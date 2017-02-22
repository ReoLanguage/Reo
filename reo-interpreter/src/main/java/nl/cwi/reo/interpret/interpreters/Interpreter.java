package nl.cwi.reo.interpret.interpreters;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import nl.cwi.reo.errors.CompilationException;
import nl.cwi.reo.errors.MyErrorListener;
import nl.cwi.reo.interpret.ReoLexer;
import nl.cwi.reo.interpret.ReoParser;
import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.components.ComponentDefinition;
import nl.cwi.reo.interpret.connectors.Semantics;
import nl.cwi.reo.interpret.connectors.SemanticsType;
import nl.cwi.reo.interpret.listeners.Listener;
import nl.cwi.reo.interpret.listeners.ReoFile;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.util.Message;
import nl.cwi.reo.util.MessageType;
import nl.cwi.reo.util.Monitor;


public class Interpreter<T extends Semantics<T>> {
	

	/**
	 * Type of semantics.
	 */
	private final SemanticsType semantics;
	
	/**
	 * ANTLR listener.
	 */
	private final Listener<T> listener;
	
	/**
	 * Component paths: base directories of component files.
	 */
	private final List<String> dirs;
	
	/**
	 * list of parameters to instantiate the main component.
	 */
	private final List<String> params;
	
	/**
	 * list of parameters to instantiate the main component.
	 */
//	private ComponentList<T> workers;
	
	
	/**
	 * Constructs a Reo interpreter.
	 * @param dirs		list of directories of Reo components
	 */
	public Interpreter(SemanticsType semantics, Listener<T> listener, List<String> dirs, List<String> params) {
		if (semantics == null || listener == null || dirs == null || params == null)
			throw new NullPointerException();
		this.semantics = semantics;
		this.listener = listener;
		this.dirs = Collections.unmodifiableList(dirs);	
		this.params = Collections.unmodifiableList(params);	
	}

	/**
	 * Interprets a list of Reo files (the first file is the main file) as a 
	 * list of atomic components.
	 * @param file		non-empty list of Reo file names.
	 * @return list of work automata.
	 */
	@SuppressWarnings("unchecked")
	public void interpret(List<String> srcfiles) {
		try {			
			// Find all available component expressions.
			Stack<ReoFile<T>> stack = new Stack<ReoFile<T>>();	
			List<String> parsed = new ArrayList<String>();
			Queue<String> components = new LinkedList<String>();
			
			for (String file : srcfiles) {
				String filename = new File(file).getName().replaceFirst("[.][^.]+$", "");
				ReoFile<T> program = parse(new ANTLRFileStream(file));
				if (program != null) {
					if (!program.getName().endsWith(filename))
						throw new CompilationException(program.getToken(), "Component must have name " + filename + ".");
					stack.push(program);
					parsed.add(program.getName());
					components.addAll(program.getImports());
				} else {
					System.err.println("[error] Cannot parse " + new File(file).getName() + ".");
				}
			}		
			
			while (!components.isEmpty()) {
				String comp = components.poll();
				if (!parsed.contains(comp)) {
					parsed.add(comp);
					ReoFile<T> program = findComponent(comp);
					if (program != null) {
						if (!program.getName().equals(comp))
							throw new CompilationException(program.getToken(), "Component must have name " + comp.substring(comp.lastIndexOf(".") + 1) + ".");
						stack.push(program);
						List<String> newComponents = program.getImports();
						newComponents.removeAll(parsed);
						components.addAll(newComponents);
					} else {
						System.err.println("[error] Component " + comp + " cannot be found.");
					}

				}
			}
		
		// Evaluate these component expressions.
		Scope scope = new Scope();
		Monitor monitor = new Monitor();
		Identifier name = null;		
		while (!stack.isEmpty()) {
			ReoFile<T> program = stack.pop();
			name = new Identifier(program.getName());
			ComponentDefinition<T> cexpr = program.getComponent().evaluate(scope,monitor);
			scope.put(name, cexpr);
		}
		
		// Get the instance from the main component.		
//		Expression expr = definitions.get(name);		
//		if (expr instanceof ReoSystemValue<?>) {				
//			ReoSystemValue<T> main = (ReoSystemValue<T>)expr;
//			ValueList values = new ValueList();
//			for (String x : params) values.add(new StringValue(x));
//			SignatureConcrete sign = main.getSignature().evaluate(values, null);
//			
//			Assembly<T> main_p = main.instantiate(values, null);
//			
//			ComponentList<T> workers = main.getWorkers();
//			ComponentList<T> instances = main_p.getInstances();
//			this.workers=workers;
//			
//			instances.insertNodes(false, false);
//			
//			return new FlatConnector<T>(instances.getComponents(), name, sign.keySet());
//		}
	} catch (IOException e) {
		System.out.print(e.getMessage());
	} catch (CompilationException e) {
		System.out.println("error"); //new Message(MessageType.ERROR, e.getToken(), e.getMessage()));
	}		
	
	}
	
	
//	public ComponentList<T> getWorkers(){
//		return workers;
//	}
	
	/**
	 * Locates the source file that contains the definition of a component.
	 * @param component		fully qualified name of the requested component.
	 * @return path string of the file containing this components definition,
	 * or null, if this path is not found.
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	private ReoFile<T> findComponent(String component) throws IOException {
		
		ReoFile<T> prog = null;
		
		int k = component.lastIndexOf('.') + 1;
		String name = component.substring(k);
		String directory = component.substring(0, k).replace('.', File.separatorChar);
		String cp1 = directory + name + "." + semantics + ".treo";
		String cp2 = directory + name + ".treo";
	
	search:
		for (String dir : dirs) {
			
			// Check if this directory contains a .zip file.
			File folder = new File(dir);
			if (folder.exists() && folder.isDirectory()) {
				
				
				FilenameFilter archiveFilter = new FilenameFilter() {
					public boolean accept(File dir, String name) {
						return name.toLowerCase().endsWith(".zip");
					}
				};	
				
				File[] files = folder.listFiles(archiveFilter);
				for (File file : files) {			
					if (!file.isDirectory()) {
					    ZipFile zipFile = null;
						try {
						    zipFile = new ZipFile(file.getPath());
						    ZipEntry entry1 = zipFile.getEntry(cp1);
						    ZipEntry entry2 = zipFile.getEntry(cp2);
						    if (entry1 != null) {
						    	InputStream input = zipFile.getInputStream(entry1);
						    	prog = parse(new ANTLRInputStream(input));
								break search;
						    } else if (entry2 != null) {
						    	InputStream input = zipFile.getInputStream(entry2);
						    	prog = parse(new ANTLRInputStream(input));
								break search;
						    }
						} finally {
							try { if (zipFile != null) zipFile.close(); } catch(IOException e) { }
						}
					} 
				}
			}
			
			File f1 = new File(dir + File.separator + cp1);
			if (f1.exists() && !f1.isDirectory()) {
				prog = parse(new ANTLRFileStream(dir + File.separator + cp1));
				break search;
			}

			File f2 = new File(dir + File.separator + cp2);
			if (f2.exists() && !f2.isDirectory()) {
				prog = parse(new ANTLRFileStream(dir + File.separator + cp2));
				break search;
			}
		}
		
		return prog;
	}
	
	/**
	 * Parses a source file using ANTLR4, and walks over the parse 
	 * tree to interpret this source file as a Java object. By default, 
	 * ANTLR4 sends any error found during parsing to System.err.
	 * @param c		input character stream
	 * @return an interpreted source file, or null in case of an error.
	 * @throws IOException 
	 */
	private ReoFile<T> parse(CharStream c) throws IOException  {
		ReoLexer lexer = new ReoLexer(c); 
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		
		ReoParser parser = new ReoParser(tokens);
		MyErrorListener errListener = new MyErrorListener();
		parser.removeErrorListeners();
		parser.addErrorListener(errListener);
		
		ParseTree tree = parser.file();
		if (errListener.hasError)
			return null;
		
		ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(listener, tree);
		return listener.getMain();
	}
	
}