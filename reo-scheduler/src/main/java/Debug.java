import nl.cwi.reo.graphgames.GameGraph;

/**
 * Test class for debugging.
 */
public class Debug {

	public static void main(String[] args) {

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
}

