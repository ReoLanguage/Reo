package nl.cwi.reo;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import nl.cwi.reo.automata.Automaton;
import nl.cwi.reo.automata.EmptyLabel;
import nl.cwi.reo.automata.Transition;
import nl.cwi.reo.compile.JavaCompiler;
import nl.cwi.reo.graphgames.GameGraph;
import nl.cwi.reo.parse.ReoFileParser;
import nl.cwi.reo.semantics.Program;
import nl.cwi.reo.workautomata.WorkAutomatonListener;

/**
 * A compiler for the coordination language Reo.
 */
public class Compiler {

	/**
	 * Entry for the Reo compiler.
	 * @param args		command line parameters
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			
			testAutomata();
			
			//testWorkAutomata();
			
			//testGameGraph();

			// Print a standard message if no arguments are given
			//System.out.println("Usage: java -jar reoc.jar <options> <reo source files>");
			//System.out.println("where possible options include:");
			//System.out.println("  -q     quiet");
			//System.out.println("  -v     version information");

		} else {
			
			File file = new File(args[0]);
			String name = file.getName();

			// Parse the Reo file.
			WorkAutomatonListener listener = new WorkAutomatonListener();
			ReoFileParser parser = new ReoFileParser(listener);
			Program program = parser.parse(args[0]);

			// Generate the classes.
			JavaCompiler JC = new JavaCompiler(name, "");
			JC.compile(program);
		}
	}
	
//	/**
//	 * Just a function to test my WorkAutomata implementation
//	 */
//	public static void testWorkAutomata() {
//		
//		WorkAutomaton A = new WorkAutomaton();
//		A.addTransition("0; 1; ; x==2");
//		A.addTransition("1; 2; ; x==2");
//		A.addTransition("1; 0; a; x<=2");
//		A.addTransition("2; 1; a; x<=2");
//		WorkAutomaton B = new WorkAutomaton();
//		B.addTransition("0; 1; a; y==1 & z==0");
//		B.addTransition("1; 0; ; y==0 & z==3");
//
//		//WorkAutomaton P = WorkAutomaton.product(A, B);
//		//A.outputDOT("A");
//		//B.outputDOT("B");
//		//P.outputDOT("P");
//
//		WorkAutomaton C = new WorkAutomaton();
//		C.addTransition("0; 0; ; x==1&y<=1");
//		C.addTransition("0; 0; ; x==5&y==0");
//		C.addTransition("0; 0; a; x==0&y==1");
//		WorkAutomaton.outputDOT(C, "C");
//
//		GameGraph G = new GameGraph(C, "a");
//		G.synthesize();
//
//		GameGraph.outputDOT(G, "schedulinggame");
//
//		// presentation
//		WorkAutomaton X = new WorkAutomaton();
//		X.addTransition("0; 0; a; x==5");
//		X.addTransition("0; 0; ; x==1");
//		WorkAutomaton Y = new WorkAutomaton();
//		Y.addTransition("0; 1; a; true");
//		Y.addTransition("1; 0; b; true");
//		WorkAutomaton Z = new WorkAutomaton();
//		Z.addTransition("0; 1; b; y==2");
//		Z.addTransition("1; 0; ; y==7");
//
//		WorkAutomaton product = WorkAutomaton.product(X, Y, Z);
//		WorkAutomaton.outputDOT(product, "product");
//	}
	
	/**
	 * Just a function to test my GameGraph implementation
	 */
	public static void testGameGraph() {
		
		GameGraph G = new GameGraph("x", 1);
		G.addEdge("x", 1, "z", 0, 3, 1);
		G.addEdge("z", 0, "w", 0, 1, 1);
		G.addEdge("w", 0, "v", 1, -4, 1);
		G.addEdge("v", 1, "w", 0, 0, 1);
		G.addEdge("v", 1, "z", 0, 1, 1);
		G.addEdge("z", 0, "y", 0, -3, 1);
		G.addEdge("y", 0, "z", 0, 1, 1);
		G.addEdge("y", 0, "x", 1, 2, 1);

		//HashMap<Vertex, Double> f = G.minimalCredit(0, 1);
		G.synthesize();

		GameGraph.outputDOT(G, "graph");
		// to render graph.dot, run:
		// dot -Tps graph.dot -o graph.ps
	}
	
	/**
	 * Just a function to test my generic automata implementation
	 */
	public static void testAutomata() {
		
		Set<String> Q = new HashSet<String>();
		Set<String> P = new HashSet<String>();
		P.add("a");
		P.add("b");
		Map<String, Set<Transition<EmptyLabel>>> T = new HashMap<String, Set<Transition<EmptyLabel>>>();
		String q0 = "q0";
		String q1 = "q1";
		Q.add(q0);
		Q.add(q1);
		Set<Transition<EmptyLabel>> out0 = new TreeSet<Transition<EmptyLabel>>();
		Set<Transition<EmptyLabel>> out1 = new TreeSet<Transition<EmptyLabel>>();
		TreeSet<String> N0 = new TreeSet<String>();
		TreeSet<String> N1 = new TreeSet<String>();
		N0.add("a");
		N1.add("b");
		Transition<EmptyLabel> t0 = new Transition<EmptyLabel>(q0, q1, N0, null);
		Transition<EmptyLabel> t1 = new Transition<EmptyLabel>(q1, q0, N1, null);
		out0.add(t0);
		out1.add(t1);
		T.put(q0, out0);
		T.put(q1, out1);
		
		Automaton<EmptyLabel> A = new Automaton<EmptyLabel>(Q, P, T, q0);

		System.out.println(A.toString());
		
		// Automaton.outputDOT(A, "A") 
		// to render the file, run:
		// dot -Tps A.dot -o A.ps
	}
}

