package nl.cwi.reo.interpret;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.cwi.reo.workautomata.WorkAutomaton;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class Interpreter {
	
	/**
	 * Interpret an input file as a component containing atoms
	 * of arbitrary semantics.
	 * @param file		input file
	 * @return Main component in the input file.
	 */
	private Component getMain(String file) {
		
		Component main = new Component();
		
		try {
			CharStream c = new ANTLRFileStream(file);
			TreoLexer lexer = new TreoLexer(c); 
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			TreoParser parser = new TreoParser(tokens);
			ParseTree tree = parser.file();
			ParseTreeWalker walker = new ParseTreeWalker();
			Listener listener = new Listener();
			walker.walk(listener, tree);
			main = listener.getMain();			
		} catch (IOException e) {
			System.out.println(file + " is invalid input.");
		}
		
		return main;
	}

	/**
	 * Interprets the main component of the input file as a list of work automata.
	 * @param file		name of the file
	 * @return	list of work automata.
	 */
	public List<WorkAutomaton> getWorkAutomata(String file) {
		
		Component main = getMain(file);
		
		List<WorkAutomaton> program = new ArrayList<WorkAutomaton>();
		
		// Counts the number output ports of atoms coincident at each node.
		Set<String> nodes = new HashSet<String>();
		Map<String, Integer> inputs = new HashMap<String, Integer>();
		Map<String, Integer> outputs = new HashMap<String, Integer>();
		
		for (Atom A : main.getAtoms()) {
			if (A.getComponent() instanceof WorkAutomaton) {
				WorkAutomaton X = (WorkAutomaton)A.getComponent();
				Map<String, String> r = new HashMap<String, String>();
				for (Map.Entry<String, String> entry : A.getLinks().entrySet()) {
					if (A.getPortType(entry.getKey()).startsWith("!")) {
						Integer k;
						if ((k = inputs.get(entry.getValue())) == null) {
							inputs.put(entry.getValue(), new Integer(1));
							r.put(entry.getKey(), entry.getValue() + "1!");
						} else {
							inputs.put(entry.getValue(), k + 1);
							r.put(entry.getKey(), entry.getValue() + k + "!");							
						}
					} else if (A.getPortType(entry.getKey()).startsWith("?")) {
						Integer k;
						if ((k = outputs.get(entry.getValue())) == null) {
							outputs.put(entry.getValue(), new Integer(1));
							r.put(entry.getKey(), entry.getValue() + "1!");
						} else {
							outputs.put(entry.getValue(), k + 1);
							r.put(entry.getKey(), entry.getValue() + k + "!");							
						}
					} else {
						// Exception : atoms have no mixed nodes
					}
				}
				X.attach(r);
				program.add(X);
			}
		}
		
		for (String a : nodes) 
			program.add(new WorkAutomaton(a, inputs.get(a), outputs.get(a)));
		
		return program;
	}
}
