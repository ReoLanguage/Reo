package nl.cwi.reo.interpret;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import nl.cwi.reo.semantics.Port;
import nl.cwi.reo.semantics.Semantics;

public final class Interpreter<T extends Semantics<T>> {
	
	/**
	 * Unit object: used to filter all instances of type T. 
	 */
	private final T unit;
	
	/**
	 * Component paths: base directories of component files.
	 */
	private final List<String> dirs;
	
	public Interpreter(T unit) {
		if (unit == null)
			throw new IllegalArgumentException("Argument cannot be null.");
		this.unit = unit;
		List<String> dirs = new ArrayList<String>();
		String comppath = System.getenv("COMPPATH");
		if (comppath != null)
			dirs.addAll(Arrays.asList(comppath.split(":")));
		this.dirs = Collections.unmodifiableList(dirs);	}

	/**
	 * Interprets the main component of the input file as a list of work automata.
	 * @param file		name of the file
	 * @return list of work automata.
	 */
	public List<T> getProgram(String mainfile) {

		// Construct a stack of all required program definitions.
		Stack<SourceFile> programs = findPrograms(mainfile);		
		
		// Evaluate this stack of program definitions.
		InstanceList instancelist = evaluatePrograms(programs);

		// Construct a set of atomic components of generic type T.
		List<T> program = new ArrayList<T>();
		
		// Split shared ports in every atom in main, and insert a node
		Map<Port, List<Port>> nodes = new HashMap<Port, List<Port>>();
		
		for (Instance comp : instancelist.getInstances()) {			
			if (comp.getAtom().getClass().equals(unit.getClass())) {	
							
				Map<String, String> r = new HashMap<String, String>();

				// For every port of this component, add the current node size as a suffix.
				for (Map.Entry<String, Port> link : comp.getLinks().entrySet()) {
					Port p = link.getValue();
					
					// Get the current node of this port, or create a new node.
					List<Port> A = nodes.get(p);
					if (A == null) {
						A = new ArrayList<Port>();
						nodes.put(p, A);
					}
					
					// Rename the port by adding a suffix.
					Port portWithSuffix = p.rename(p.getName() + A.size());
					
					// Add the renamed port to this node.
					A.add(portWithSuffix);

					nodes.put(link.getValue(), A);
					
					// Register how to rename the ports in the semantics.
					r.put(link.getKey(), portWithSuffix.getName());
				}
				
		    	@SuppressWarnings("unchecked")
				T X = ((T)comp.getAtom()).rename(r);
				program.add(X);
			}
		}
		
		for (Map.Entry<Port, List<Port>> node : nodes.entrySet()) 
			if (node.getValue().size() > 1)
				program.add(unit.getNode(node.getValue()));
		
		return program;
	}
	
	/**
	 * Constructs a stack of all required program definitions, by tracing all 
	 * component imports in every required files.  
	 * @param mainfile		initial file, containing the main component.
	 * @return stack of component definitions, whose bottom is the main component.
	 */
	private Stack<SourceFile> findPrograms(String mainfile) {
		Stack<SourceFile> programs = new Stack<SourceFile>();	
		List<String> includedFiles = new ArrayList<String>();
		Queue<String> files = new LinkedList<String>();
		files.add(mainfile);		
		while (!files.isEmpty()) {
			String file = files.poll();
			includedFiles.add(file);			
			SourceFile program = parseFile(file);			
			if (program != null) {
				programs.push(program);
				for (String comp : program.getImports()) {
					String new_file = findSoureFile(comp);
					if (new_file == null)
						System.out.println("Component " + comp + " could not be found.");
					if (!includedFiles.contains(new_file))
						files.add(new_file);
				}
			} else {
				System.out.println("File " + file + " could not be parsed.");				
			}
		}
		return programs;
	}
	
	/**
	 * Evaluate a stack of program definitions by repeated substitution.
	 * @param programs		stack of program definitions.
	 * @return ComponentValue
	 */
	private InstanceList evaluatePrograms(Stack<SourceFile> programs) {
		
		BodyDefinitionList definitions = new BodyDefinitionList();
		VariableName main = null;		
		try {
			while (!programs.isEmpty()) {
				SourceFile program = programs.pop();
				main = program.getVariableName();
				definitions.putAll(program.evaluate(definitions));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Expression pexpr = definitions.get(main);
		
		// Check if the evaluated program expression is a component value.
		if (!(pexpr instanceof ComponentValue)) 
			return new InstanceList();
				
		return ((ComponentValue)pexpr).getInstanceList();
	}
	
	/**
	 * Parses a source file using ANTLR4, and walks over the parse 
	 * tree to interpret this source file as a Java object. By default, 
	 * ANTLR4 sends any error found during parsing to System.err.
	 * @param file		source file
	 * @return an interpreted source file, or null in case of an error.
	 */
	private SourceFile parseFile(String file) {		
		CharStream c = null;		
		try {
			c = new ANTLRFileStream(file);
		} catch (IOException e) {
			System.out.println("Cannot open file " + file + ".");
		}
		if (c == null) return null;
		TreoLexer lexer = new TreoLexer(c); 
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		TreoParser parser = new TreoParser(tokens);
		ParseTree tree = parser.file();
		ParseTreeWalker walker = new ParseTreeWalker();
		Listener listener = new Listener();
		walker.walk(listener, tree);
		return listener.getFile();
	}
	
	/**
	 * Locates the source file that contains the definition of a component.
	 * @param comp		fully qualified name of the requested component.
	 * @return path string of the file containing this components definition,
	 * or null, if this path is not found.
	 */
	private String findSoureFile(String comp) {
		
		String[] names = comp.split("\\.");
		names[names.length - 1] += ".treo";
		
		for (String dir : dirs) {
			Path path = Paths.get(dir, names);
			String filename = path.toString();
			File f = new File(filename);
			if (f.exists() && !f.isDirectory()) 
				return filename;
		}
		
		return null;
	}
	
}
