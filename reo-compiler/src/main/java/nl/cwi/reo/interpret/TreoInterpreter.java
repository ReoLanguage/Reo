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

public final class TreoInterpreter<T extends Semantics<T>> {
	
	private final T unit;
	
	private final List<String> dirs;
	
	public TreoInterpreter(T unit) {
		if (unit == null)
			throw new IllegalArgumentException("Argument cannot be null.");
		this.unit = unit;
		List<String> dirs = new ArrayList<String>();
		String comppath = System.getenv("COMPPATH");
		if (comppath != null)
			dirs.addAll(Arrays.asList(comppath.split(":")));
		this.dirs = Collections.unmodifiableList(dirs);
	}

	/**
	 * Interprets the main component of the input file as a list of work automata.
	 * @param file		name of the file
	 * @return list of work automata.
	 */
	public List<T> getProgram(String mainfile) {
		
		VariableName vmain = null; 
		
		// A stack of program expressions, whose bottom is the main program expression.
		Stack<DefinitionMain> programs = new Stack<DefinitionMain>();	
		
		// A list of already included components.
		List<String> includedFiles = new ArrayList<String>();
		
		// A queue of files that need to be interpreted.
		Queue<String> files = new LinkedList<String>();
		files.add(mainfile);
		
		while (!files.isEmpty()) {
			
			// Grab the head of the queue, and mark it as imported. 
			String file = files.poll();
			includedFiles.add(file);
			
			try {
				// Parse the file
				CharStream c = new ANTLRFileStream(file);
				TreoLexer lexer = new TreoLexer(c); 
				CommonTokenStream tokens = new CommonTokenStream(lexer);
				TreoParser parser = new TreoParser(tokens);
				ParseTree tree = parser.file();
				
				// Interpret the file
				ParseTreeWalker walker = new ParseTreeWalker();
				TreoProgramListener listener = new TreoProgramListener();
				walker.walk(listener, tree);
				DefinitionMain program = listener.getFile();	
				
				// Add the program to the stack
				programs.push(program);
				if (vmain == null)
					vmain = program.getVariableName();
				
				// Find new source files from imports, and, if necessary, add them to the list.
				for (String comp : program.getImports()) {
					String new_file = findSoureFile(comp);
					if (new_file == null)
						System.out.println("Component " + comp + " could not be found.");
					if (!includedFiles.contains(new_file))
						files.add(new_file);
				}
			} catch (IOException e) {
				System.out.println("Cannot open file " + file + ".");
			}
		}

		// Try to evaluate the stack of program expressions
		DefinitionList defs = new DefinitionList();
		try {
			while (!programs.isEmpty()) 
				defs.putAll(programs.pop().evaluate(defs));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Get the main component from the program.
		List<T> program = new ArrayList<T>();
		ComponentValue main;
		System.out.println("var main " + vmain);
		Expression v = defs.get(vmain);		
		System.out.println("expression" + v);
		if (v instanceof ComponentValue) {
			main = (ComponentValue)v;
		} else {
			return program;
		}
		
		
		// Split shared ports in every atom in main, and insert a node
		Map<Port, List<Port>> nodes = new HashMap<Port, List<Port>>();
		
		for (Map.Entry<Semantics<?>, Map<String, Port>> atom : main.getInstance().getAtomics().entrySet()) {			
			if (atom.getKey().getClass().equals(unit.getClass())) {	
							
				Map<String, String> r = new HashMap<String, String>();

				// For every port of this component, add the current node size as a suffix.
				for (Map.Entry<String, Port> link : atom.getValue().entrySet()) {	
					// Get the current node of this port, or create a new node.
					List<Port> node = nodes.get(link.getValue().getName());
					if (node == null)
						node = new ArrayList<Port>();
					
					// Rename the port by adding a suffix.
					Port portWithSuffix = link.getValue().addSuffix("-" + node.size());
					
					// Add the renamed port to this node.
					node.add(portWithSuffix);
					
					// Register how to rename the ports in the semantics.
					r.put(link.getKey(), portWithSuffix.getName());
				}
				
		    	@SuppressWarnings("unchecked")
				T X = (T)atom.getKey();					
				X.rename(r);				
				program.add(X);
			}
		}
		
		for (Map.Entry<Port, List<Port>> node : nodes.entrySet()) 
			program.add(unit.getNode(node.getValue()));
		
		return program;
	}
	
	private String findSoureFile(String comp) {
		
		String[] names = comp.split("\\.");
		names[names.length - 1] += ".treo";
		
		for (String dir : dirs) {
			Path path = Paths.get(dir, names);
			String filename = path.toString();
			System.out.println("checking path " + filename);
			File f = new File(filename);
			if (f.exists() && !f.isDirectory()) 
				return filename;
		}
		
		return null;
	}
	
}
