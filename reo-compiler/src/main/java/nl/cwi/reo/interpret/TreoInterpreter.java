package nl.cwi.reo.interpret;

import java.io.IOException;
import java.util.ArrayList;
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

public class TreoInterpreter<T extends Semantics<T>> {
	
	private T unit;
	
	public TreoInterpreter(T unit) {
		this.unit = unit;
	}

	/**
	 * Interprets the main component of the input file as a list of work automata.
	 * @param file		name of the file
	 * @return list of work automata.
	 */
	public List<T> getProgram(String mainfile) {
		
		// A stack of program expressions, whose bottom is the main program expression.
		Stack<File> programs = new Stack<File>();	
		
		// A list of marked imports.
		List<String> imports = new ArrayList<String>();
		
		// A queue to to do imports.
		Queue<String> todo = new LinkedList<String>();
		todo.add(mainfile);
		
		while (!todo.isEmpty()) {
			
			// Grab the head of the queue, and mark it as imported. 
			String file = todo.poll();
			imports.add(file);
			
			// Try to parse and interpret the program in this file.
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
				File program = listener.getProgramSection();	
				
				// Add the program to the stack
				programs.push(program);
				
				// Add imports of program to the queue, if not yet imported
				for (String importfile : program.getImports()) 
					if (!imports.contains(importfile))
						imports.add(importfile);

			} catch (IOException e) {
				System.out.println("File " + file + " cannot be found.");
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
		Expression v = defs.getMain();		
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
}
