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
}

