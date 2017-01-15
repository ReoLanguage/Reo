package nl.cwi.reo.interpret;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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

import nl.cwi.reo.interpret.components.ComponentValue;
import nl.cwi.reo.interpret.listeners.Listener;
import nl.cwi.reo.interpret.programs.ProgramFile;
import nl.cwi.reo.interpret.ranges.Expression;
import nl.cwi.reo.interpret.semantics.InstanceList;
import nl.cwi.reo.interpret.variables.VariableName;
import nl.cwi.reo.semantics.Semantics;
import nl.cwi.reo.semantics.SemanticsType;

public class Interpreter<T extends Semantics<T>> {
	
	/**
	 * Type of semantics.
	 */
	private final SemanticsType semantics;
	
	/**
	 * Component paths: base directories of component files.
	 */
	private final List<String> dirs;
	
	/**
	 * ANTLR listener.
	 */
	private final Listener<T> listener;
	
	/**
	 * Constructs a Reo interpreter.
	 * @param dirs		list of directories of Reo components
	 */
	public Interpreter(SemanticsType semantics, List<String> dirs, Listener<T> listener) {
		this.semantics = semantics;
		this.dirs = Collections.unmodifiableList(dirs);	
		this.listener = listener;
	}

	/**
	 * Interprets a list of Reo files (the first file is the main file) as a 
	 * list of atomic components.
	 * @param file		non-empty list of Reo file names.
	 * @return list of work automata.
	 */
	@SuppressWarnings("unchecked")
	public List<T> interpret(List<String> srcfiles) {
		
		// Find all available component expressions.
		Stack<ProgramFile<T>> stack = new Stack<ProgramFile<T>>();	
		try {
			List<String> parsed = new ArrayList<String>();
			Queue<String> components = new LinkedList<String>();
			
			for (String file : srcfiles) {
				ProgramFile<T> program = parse(new ANTLRFileStream(file));
				if (program != null) {
					stack.push(program);
					parsed.add(program.getName());
					components.addAll(program.getImports());
				} else {
					System.out.println("[error] File " + file + " could not be parsed.");
				}
			}		
			
			while (!components.isEmpty()) {
				String comp = components.poll();
				if (!parsed.contains(comp)) {
					parsed.add(comp);
					ProgramFile<T> program = parseComponent(comp);
					if (program != null) {
						stack.push(program);
						List<String> newComponents = program.getImports();
						newComponents.removeAll(parsed);
						components.addAll(newComponents);
					} else {
						System.out.println("[error] Component " + comp + " cannot be found.");
					}

				}
			}
		} catch (IOException e) {
			System.out.print(e.getMessage());
		}		

		// Evaluate these component expressions.
		Map<VariableName, Expression> cexprs = new HashMap<VariableName, Expression>();
		VariableName name = null;		
		try {
			while (!stack.isEmpty()) {
				ProgramFile<T> program = stack.pop();
				name = new VariableName(program.getName());
				Expression cexpr = program.getComponent().evaluate(cexprs);
				cexprs.put(name, cexpr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Get the instance from the main component.
		InstanceList<T> instances = null;
		Expression expr = cexprs.get(name);		
		if (expr instanceof ComponentValue<?>) {
			instances = ((ComponentValue<T>)expr).getInstances();
			
			instances.insertNodes(true, false);
			
			return instances.getInstances();
		}
		return new ArrayList<T>();
	}
	
	/**
	 * Locates the source file that contains the definition of a component.
	 * @param component		fully qualified name of the requested component.
	 * @return path string of the file containing this components definition,
	 * or null, if this path is not found.
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	private ProgramFile<T> parseComponent(String component) throws IOException {
		
		ProgramFile<T> prog = null;
		
		int k = component.lastIndexOf('.') + 1;
		String name = component.substring(k);
		String directory = component.substring(0, k).replace('.', File.separatorChar);
		String cp1 = directory + semantics + File.separator + name + ".treo";
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
	private ProgramFile<T> parse(CharStream c) throws IOException  {
		ReoLexer lexer = new ReoLexer(c); 
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		
		ReoParser parser = new ReoParser(tokens);
		parser.removeErrorListeners();
		parser.addErrorListener(new ErrorListener());
		
		ParseTree tree = parser.file();
		ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(listener, tree);
		return listener.getFile();
	}
	
}
