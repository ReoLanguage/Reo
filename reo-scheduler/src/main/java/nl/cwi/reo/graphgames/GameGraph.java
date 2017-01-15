package nl.cwi.reo.graphgames;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import nl.cwi.reo.semantics.Port;
import nl.cwi.reo.workautomata.Transition;
import nl.cwi.reo.workautomata.WorkAutomaton;

/**
 * Implements a bipartite directed graph whose edges are labeled by two integers.
 */
public class GameGraph {

	/**
	 * Set of vertices
	 */
	public HashMap<String, Vertex> vert;

	/**
	 * Adjacency list 
	 */
	public HashMap<Vertex, Set<Edge>> adjc;

	/**
	 * Initial vertex
	 */
	public Vertex v0;

	/**
	 * Number of vertices
	 */
	public int V;

	/**
	 * Number of edges
	 */
	public int E;

	/**
	 * Vertices with value larger than the value of v0
	 */
	public HashSet<Vertex> vert0;

	/**
	 * Edges that secure a value larger than the value of v0
	 */
	public HashMap<Vertex, Set<Edge>> adjc0;

	/**
	 * Constructs an empty GameGraph.
	 * @param v0	initial vertex of the game
	 * @param owner	player that owns the initial vertex
	 */
	public GameGraph(String v0, int owner) {
		this.adjc = new HashMap<Vertex, Set<Edge>>();
		this.vert = new HashMap<String, Vertex>();
		this.adjc0 = new HashMap<Vertex, Set<Edge>>();
		this.vert0 = new HashSet<Vertex>();
		this.v0 = addVertex(new Vertex(v0, owner));
		this.V = 0;
		this.E = 0;
	}

	/**
	 * Constructs a GameGraph from a WorkAutomaton.
	 * @param A			work automaton
	 * @param port		port where throughput is measured
	 */
	public GameGraph(WorkAutomaton A, String port) {
		this.adjc = new HashMap<Vertex, Set<Edge>>();
		this.vert = new HashMap<String, Vertex>();
		this.adjc0 = new HashMap<Vertex, Set<Edge>>();
		this.vert0 = new HashSet<Vertex>();
		this.V = 0;
		this.E = 0;

		// Construct initial vertex a0
		TreeMap<String, Integer> p0 = new TreeMap<String, Integer>();
		for (String x : A.getJobs()) p0.put(x, 0);
		TreeMap<String, Integer> d0 = new TreeMap<String, Integer>();  
		for (String x : A.getJobs()) d0.put(x, 0);
		SGVertex a0 = new SGVertex(p0, A.getInitial(), new TreeSet<Port>(), d0);
		this.v0 = addVertex(a0);

		// Add a0 to the list L of unexplored vertices
		HashSet<SGVertex> L = new HashSet<SGVertex>();
		L.add(a0);
		
		while (!L.isEmpty()) {

			// Get an unexplored vertex from L
			SGVertex a = L.iterator().next();
			L.remove(a);

			if (a.owner == 0) { // if a = (p, q, N, d)

				// Iterate over all possible subset s of J
				for (long i = 0; i < Math.pow(2, A.getJobs().size()); ++i) {

					// Construct subset s of J using the binary representation of i
					List<String> jobs = new ArrayList<String>(A.getJobs());
					TreeSet<String> s = new TreeSet<String>();
					for (int k = 0; k < jobs.size(); ++k) 
						if (((i >> k) & 1) == 1) 
							s.add(jobs.get(k));	

					// Construct the next vertex b
					SGVertex b = new SGVertex(a.p, a.q, s);

					// If vertex b is new, add b to L
					if (vert.get(b.name) == null) L.add(b);

					// Add the transition from a to b
					addEdge(a, b, 0, 0);

				}

			} else { // if a = [p, q, s]

				boolean hasEnabled = false;

				// Iterate over all transitions tau at q in A.
				for (Transition tau : A.getTransitions(a.q)) {

					// Compute the time to first completion of tau
					int t = ttfc(tau, a.p, a.s);

					// Check if transition tau is enabled 
					if (t >= 0) {
						hasEnabled = true;

						// Compute d
						TreeMap<String,Integer> d = new TreeMap<String, Integer>();  
						for (String x : A.getJobs()) d.put(x, a.s.contains(x) ? t : 0);

						// Compute p
						TreeMap<String,Integer> p = new TreeMap<String, Integer>(); 
						for (String x : A.getJobs()) p.put(x, a.p.get(x) + d.get(x));

						// Determine if transition tau fires
						boolean fires = true;
						for (String x : A.getJobs()) 
							if (p.get(x) > tau.getJobConstraint().getW().get(x) || (tau.getJobConstraint().getR().contains(x) && p.get(x) < tau.getJobConstraint().getW().get(x)))
								fires = false;

						//System.out.println("w=" + tau.w);
						//System.out.println("R=" + tau.R);
						//System.out.println("p=" + p);
						//System.out.println(fires);

						Set<Port> N;

						int w = 0;
						
						if (fires) { 
							for (String x : tau.getJobConstraint().getR()) p.put(x, 0);
							N = tau.getSyncConstraint();
							w = tau.getSyncConstraint().contains(port) ? 1 : 0;
						} else {
							N = new TreeSet<Port>();
						}

						// Construct the next vertex b
						SGVertex b = new SGVertex(p, a.q, N, d);

						// If vertex b is new, add b to L
						if (vert.get(b.name) == null) L.add(b);

						// Add the transition from a to b
						addEdge(a, b, w, t);
					}
				}

				// If no edge has been added to vertex a, then add a waiting move
				if (!hasEnabled) {

					// Construct the next vertex b
					TreeMap<String,Integer> d = new TreeMap<String, Integer>();  
					for (String x : A.getJobs())  d.put(x, 0);
					SGVertex b = new SGVertex(a.p, a.q, new TreeSet<Port>(), d);

					// If vertex b is new, add b to L
					if (vert.get(b.name) == null) L.add(b);

					// Determine time of idling transition
					int t = A.getTransitions(a.q).isEmpty() ? 0 : 1;

					// Add the transition from a to b
					addEdge(a, b, 0, t);
				}
			}
		}
	}

	/**
	 * Computes the time to first completion of a transition.
	 * 
	 * @param tau		transition
	 * @param p			current progress of jobs 
	 * @param s			set of scheduled/running jobs
	 * @return time to first completion, or -1 if tau is not enabled.
	 */
	private static int ttfc(Transition tau, Map<String, Integer> p, Set<String> s) {
		
		// Check if p potentially satisfies the job constraint of tau
		for (Map.Entry<String, Integer> entry : tau.getJobConstraint().getW().entrySet()) 
			if ( p.get(entry.getKey()) > entry.getValue() )
				return -1;

		// If no jobs are scheduled then t = 0, if p satisfies w, and t = -1 otherwise
		if (s.isEmpty()) {
			boolean satisfy = true;
			for (Map.Entry<String, Integer> entry : tau.getJobConstraint().getW().entrySet()) 
				if ( tau.getJobConstraint().getR().contains(entry.getKey()) && p.get(entry.getKey()) != entry.getValue() )
					satisfy = false;
			return satisfy ? 0 : -1;
		}

		// Compute the time to completion of every scheduled job
		ArrayList<Integer> times = new ArrayList<Integer>();
		for (String x : s)
			times.add(tau.getJobConstraint().getW().get(x) - p.get(x));

		// Return the minimal time to completion if its nonzero and -1 otherwise
		int min = Collections.min(times);
		return min == 0 ? -1 : min;
	}


	/**
	 * Adds a vertex v, if not yet present.
	 *  
	 * @param a		vertex
	 * @return reference to the new, or existing, vertex.
	 */
	public Vertex addVertex(Vertex a) {
		Vertex v;
		v = vert.get(a.name);
		if (v == null) {
			v = a;
			vert.put(v.name, a);
			vert0.add(v);
			adjc.put(v, new TreeSet<Edge>());
			adjc0.put(v, new TreeSet<Edge>());
			V += 1;
		}
		return v;
	}
	
	/**
	 * Adds an edge to the GameGraph, if it does not exists.
	 *
	 * @param a		name of the first vertex
	 * @parem i		owner of the first vertex
	 * @param b		name of the second vertex
	 * @parem j		owner of the second vertex
	 * @param w		weight of the edge
	 * @param t		time of the edge
	 */
	public void addEdge(String a, int i, String b, int j, int w, int t) {
		addEdge(new Vertex(a, i), new Vertex(b, j), w, t);
	}

	/**
	 * Adds an edge to the GameGraph, if it does not exists. 
	 *
	 * @param a		source vertex
	 * @param b		target vertex
	 * @param w		weight of the egde
	 * @param t		time of the edge
	 */
	public void addEdge(Vertex a, Vertex b, int w, int t) {
		Vertex a1 = addVertex(a);
		Vertex b1 = addVertex(b);
		Edge e = new Edge(a1, b1, w, t);
		if (adjc.get(a1).contains(e)) return;
		E += 1;
		adjc.get(a1).add(e);
		adjc.get(b1).add(e);
		adjc0.get(a1).add(e);
		adjc0.get(b1).add(e);
	}

	/**
	 * Produces a .dot file containing the game graph;
	 *
	 * @param G			game graph
	 * @param fileName	the name of the file without .dot extension
	 * @return <code>true</code> if the file is successfully written.
	 */
	public static boolean outputDOT(GameGraph G, String fileName) {
		HashMap<Vertex, String> idToName = new HashMap<Vertex, String>();
		try {
			FileWriter out = new FileWriter(fileName + ".dot");
			int count = 0;
			out.write("// This game graph contains " + G.V + " vertices and " + G.E + " edges.\n");
			out.write("digraph {\n\toverlap=scale;\n\tsplines=true;\n");
			for (Vertex v : G.vert.values()) {
				String id = "v" + count++;
				idToName.put(v, id);
				String label = "label=\"" + v.name + "\"";
				String color = v.equals(G.v0) ? "color=red" : "color=black";
				String shape = v.owner == 0 ? "shape=oval" : "shape=diamond";
				String attributes = " [" + label + ","+ shape + ","+ color + "];\n";
				out.write("\t" + id + attributes);
			}
			for (Vertex v : G.vert.values()) {
				for (Edge e : G.adjc.get(v)) {
					if (v.equals(e.source)) {
						String optimal = (G.vert0.contains(v) && G.adjc0.get(v).contains(e)) ? "style=solid" : "style=dotted";
						String label = v.owner == 0 ? "label=\"0/0\"" : "label=\"" + e.weight + "/" + e.time + "\"";
						String attributes = " [" + label + ","+ optimal + "];\n";
						out.write("\t" + idToName.get(v) + " -> "+ idToName.get(e.target) + attributes);
					}
				}
			}
			out.write("}");
			out.close();
		} catch (IOException e) {
			return false;
			//e.printStackTrace();
		}
		return true;
	}

	/**
	 * Computes minimal credit function on the energy game associated
	 * with the ratio game with winning condition <code>value &gt;= v</code>, for 
	 * some fraction v = p/q.
	 * 
	 * @param p		numerator of value v
	 * @param q		denominator of value
	 * @return the minimal credit function on the energy game 
	 * that contains only optimal vertices and edges with weights 
	 * w'(e) = q * w(e) - p * t(e), for every edge e
	 */
	public HashMap<Vertex, Double> minimalCredit(int p, int q) {
		HashMap<Vertex, Double> f = new HashMap<Vertex, Double>();
		HashMap<Vertex, Integer> count = new HashMap<Vertex, Integer>();
		Set<Vertex> L = new HashSet<Vertex>();
		int M = 0;

		for (Vertex v : vert0) {
			int cnt = 0;
			boolean ic = false;
			List<Integer> w = new ArrayList<Integer>();
			w.add(0);

			for (Edge e : adjc.get(v)) {
				if (v.equals(e.source) && vert0.contains(e.target)) {
					int we = q * e.weight - p * e.time; 
					w.add(-we); 
					if (v.owner == 0 && we >= 0) cnt++;
					if (v.owner == 1 && we < 0) ic = true;
				}
			}

			f.put(v, 0.0);
			count.put(v, cnt);
			if ((v.owner == 0 && cnt == 0) || (v.owner == 1 && ic)) L.add(v);
			M += Collections.max(w); // is this still correct in throughput games?
		}

		while (!L.isEmpty()) {

			Vertex v = L.iterator().next();
			L.remove(v);
			Double oldf = f.get(v);

			// Compute the new value of f at v
			List<Double> c = new ArrayList<Double>();
			for (Edge e : adjc.get(v)) 
				if (v.equals(e.source) && vert0.contains(e.target))
					c.add(f.get(e.target) - q * e.weight + p * e.time);
			Double newf = v.owner == 0 ? Collections.min(c) : Collections.max(c);
			if (newf < 0.0) newf = 0.0;
			if (newf > M) newf = Double.POSITIVE_INFINITY;
			f.put(v, newf);

			// Update the counter at v
			if (v.owner == 0) {
				int cnt = 0;
				for (Edge e : adjc.get(v)) 
					if (v.equals(e.source) && vert0.contains(e.target)) 
						if (f.get(e.source) + q * e.weight - p * e.time >= f.get(e.target))
							cnt++;
				count.put(v, cnt);
			}

			// Find new inconsistencies and update their counters
			for (Edge e : adjc.get(v)) {
				if (v.equals(e.target) && vert0.contains(e.source)) {
					if (f.get(e.source) + q * e.weight - p * e.time < f.get(e.target)) {
						if (e.source.owner == 0) {
							if (f.get(e.source) + q * e.weight - p * e.time >= oldf) 
								count.put(e.source, count.get(e.source) - 1);
							if (count.get(e.source) <= 0) 
								L.add(e.source);
						} else {
							L.add(e.source);
						}
					}
				}
			}
		}
	
		return f;
	}

	/**
	 * Removes all vertices from the game graph that are not used
	 * by some optimal strategy.
	 */
	public void synthesize() {	

		// This code assumes that there the minimal weight is <= 0 and the maximal weight is >= 0.
		int V0 = V;
		int pmin = 0;
		int qmin = 1;
		int pmax = 0;
		int qmax = 1;

		for (Vertex v : vert0) {
			for (Edge e : adjc.get(v)) {
				pmin = Math.min(pmin, V0 * e.weight);
				pmax = Math.max(pmax, V0 * e.weight);
			}
		}

		while (V0 * (V0-1) * (pmax * qmin - pmin * qmax) >= qmin * qmax) {

			int g = gcd(pmin * qmax + pmax * qmin, 2 * qmin * qmax);
			int p = (pmin * qmax + pmax * qmin) / g;
			int q = (2 * qmin * qmax) / g;

			HashMap<Vertex, Double> f = minimalCredit(p, q);
			//System.out.println("p0/q0 = " + pmin + "/" + qmin + "; " + "p/q = " + p + "/" + q + "; " + "p1/q1 = " + pmax + "/" + qmax + "; ");		
			//System.out.println("f = " + f);		

			// the code does not handle atomic cycles, i.e., cycles C with \sum t(C) = 0
			
			// Let G0 be the reachable part of G0(f)
			V0 = 0;
			HashSet<Vertex> reachable = new HashSet<Vertex>();
			HashSet<Vertex> L = new HashSet<Vertex>();
			L.add(v0);
			while (!L.isEmpty()) {
				Vertex v = L.iterator().next();
				L.remove(v);
				reachable.add(v);
				V0++;
				TreeSet<Edge> delete = new TreeSet<Edge>();
				for (Edge e : adjc0.get(v)) {
					if (v.equals(e.source)) {
						if (f.get(e.source) + q * e.weight - p * e.time < f.get(e.target)) delete.add(e);
						if (!reachable.contains(e.target)) L.add(e.target);
					}
				}
				for (Edge e : delete) 
					adjc0.get(v).remove(e);
			}

			vert0 = reachable;
			//System.out.println(vert0);

			if (f.get(v0) < Double.POSITIVE_INFINITY) { // if val(v0) >= p/q
				pmin = p;
				qmin = q;
			} else { // if val(v0) < p/q
				pmax = p;
				qmax = q;
			}
		}

		//System.out.println(vert0);
	}

	/**
	 * Computes the greatest common divisor of a and b.
	 * 
	 * @param a 	integer
	 * @param b 	integer
	 * @return <code>gcd(a,b)</code>
	 */
	private static int gcd(int a, int b) { 
		return b==0 ? Math.abs(a) : gcd(b, a % b); 
	}
}
