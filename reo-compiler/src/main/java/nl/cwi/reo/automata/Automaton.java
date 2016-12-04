package nl.cwi.reo.automata;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.HashMap;

/**
 * A port automaton in which each transition has, besides a synchronization constraint, a generic label of type L.
 */
public final class Automaton<L extends Label<L>> {

	/**
	 * Set of states
	 */
	private final Set<String> Q;

	/**
	 * Set of ports
	 */
	private final Set<String> P;

	/**
	 * List of outgoing transitions.
	 */
	private final Map<String, Set<Transition<L>>> T;

	/**
	 * Initial state.
	 */
	private final String q0;

	/**
	 * Constructs a single state work automaton which is the identity with respect to composition.
	 */
	public Automaton() {
		this.Q = new HashSet<String>();
		this.P = new HashSet<String>();
		this.T = new HashMap<String, Set<Transition<L>>>();
		this.q0 = "q0";
		Q.add(q0);
		T.put(q0, new TreeSet<Transition<L>>());
	}
	
	/**
	 * Constructs a new work automaton.
	 * @param Q		set of states
	 * @param P		set of ports
	 * @param I		mapping from states to invariant conditions
	 * @param T		mapping from states to outgoing transitions
	 * @param q0 	initial state
	 */
	public Automaton(Set<String> Q, Set<String> P, Map<String, Set<Transition<L>>> T, String q0) {
		this.Q = Q;
		this.P = P;
		this.T = T;
		this.q0 = q0;
	}
	
	/**
	 * Gets the set of states of this work automaton.
	 */
	public Set<String> getStates() {
		return this.Q;
	}
	
	/**
	 * Gets the set of ports in the interface of this work automaton.
	 */
	public Set<String> getInterface() {
		return this.P;
	}
	
	/**
	 * Gets the set of outgoing transitions of a state of the work automaton.
	 * @param q		state
	 * @return set of outgoing transitions from the given state.
	 */
	public Set<Transition<L>> getTransitions(String q) {
		return this.T.get(q);
	}
	
	/**
	 * Gets the initial state of the work automaton.
	 */
	public String getInitial() {
		return this.q0;
	}
	
	/**
	 * Connects the ports of the work automaton to nodes.
	 * @param links		relabeling function
	 */
	public Automaton<L> attach(Map<String, String> links) {
		
		// Initialize the automaton
		Set<String> Q = new HashSet<String>(this.Q);
		Set<String> P = new HashSet<String>(this.P);
		Map<String, Set<Transition<L>>> T = new HashMap<String, Set<Transition<L>>>();
		
		// Rename the ports in the interface
		for (String a : this.P) {
			String port;
			if ((port = links.get(a)) == null)
				port = a;
			P.add(port);
		}
		
		// Add relabeled transitions to the set of transition
		for (Set<Transition<L>> outgoing : this.T.values())
			for (Transition<L> t : outgoing) 
				T.get(t.getSource()).add(t.rename(links));
		
		return new Automaton<L>(Q, P, T, this.q0);
	}
	
	/**
	 * Gets the string representation of this automaton in .dot format.
	 */
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		 
		str.append("// Automaton with interface " + this.P + "\n");
		str.append("digraph {\n");
		for (String q : this.Q) 
			str.append("\t" + q + " [label=\"" + q + "\"];\n");
		for (String q : this.Q) {
			for (Transition<L> t : this.T.get(q)) {
				String q1 = t.getSource();
				String q2 = t.getTarget();
				String sc = t.getSyncConstraint().toString();
				str.append("\t" + q1 + " -> "+ q2 + " [label=\"" + sc + "\"];\n");
			}
		}
		str.append("}");
		 
		return str.toString();
	}
	
	/**
	 * Produces a .dot file containing the work automaton.
	 * @param A				work automaton
	 * @param fileName		the name of the file without .dot extension
	 * @return true if file is successfully written.
	 */
	public static <L extends Label<L>> boolean outputDOT(Automaton<L> A, String fileName) {
		try {
			FileWriter out = new FileWriter(fileName + ".dot");
			out.write(A.toString());
			out.close();
		} catch (IOException e) {
			return false;
		} 
		return true;
	}

	/**
	 * Computes the product of a list of work automata by means of
	 * a breadth first algorithm.
	 * @param automata		a list of work automata
	 * @return the identity automaton, if the list is empty; the 
	 * first automaton, if the list has size one; and the product 
	 * work automaton, if the list contains at least two automata.
	 */
	public static <L extends Label<L>> Automaton<L> product(List<Automaton<L>> automata) {
		
		// Get the number of work automata.
		int size = automata.size();
		
		// If the product is empty, return the identity automaton.
		if (size == 0) 
			return new Automaton<L>();
		
		// If the product is trivial, return the unique work automaton.
		if (size == 1) 
			return automata.get(0);

		// Initialize the automaton fields.
		Set<String> Q = new HashSet<String>();
		Set<String> P = new HashSet<String>();
		Map<String, Set<Transition<L>>> T = new HashMap<String, Set<Transition<L>>>();
		String q0;

		// Find the global initial state.
		List<String> s0 = new ArrayList<String>();
		for (int i = 0; i < size; i++) 
			s0.add(automata.get(i).q0);
		q0 = compose(s0);
		
		// Initialize the queue L of unexplored global states.
		Queue<List<String>> L = new LinkedList<List<String>>();
		
		// Add the initial state to the queue.
		L.add(s0);
		Q.add(q0);
		T.put(q0, new TreeSet<Transition<L>>());
		
		// Just for fun: count the actual and total number of steps to find composable tuples of local transitions.
		int actual = 0;
		int total = 0;
		
		// Loop until the queue is empty.
		while (!L.isEmpty()) {
			
			// Get and remove the first unexplored global state in L.
			List<String> s1 = L.poll();
			
			// Add global state q1 to the set of states Q
			Q.add(compose(s1));

			// For each work automaton A.get(i), find the list localtransitions.get(i) of all possible 
			// local transitions from the local state s1.get(i).
			List<List<Transition<L>>> localtransitions = new ArrayList<List<Transition<L>>>();
			for (int i = 0; i < size; i++) 
				localtransitions.add(new ArrayList<Transition<L>>(automata.get(i).T.get(s1.get(i))));
			
			// Iterate over all *composable* combinations of local transitions from s1.
			TransitionIterator<L> combination = new TransitionIterator<L>(automata, s1, localtransitions);

			// Iterate over all combinations of local transitions.
			while (combination.hasNext()) {
				
				// Get the next composable combination of local transitions.
				List<Transition<L>> tuple = combination.next();
				
				// Construct the source and target state, label, and synchronization constraint.
				SortedSet<String> N = new TreeSet<String>();
				List<L> lbls = new ArrayList<L>();
				List<String> s2 = new ArrayList<String>();
				for (int i = 0; i < size; i++) {
					N.addAll(tuple.get(i).getSyncConstraint());
					lbls.add(tuple.get(i).getLabel());
					s2.add(tuple.get(i).getTarget());
				}
				L lbl = tuple.get(0).getLabel().compose(lbls);
				String q1 = compose(s1);
				String q2 = compose(s2);
				
				// Construct the global transition.
				Transition<L> t = new Transition<L>(q1, q2, N, lbl);

				// Add the target state to the work automaton, and add it to the queue if its new.
				if (!Q.add(q2)) 
					L.add(s2);
				
				// Add the global transition to the work automaton
				T.get(q1).add(t);				
			}
			
			// Just for fun: add the total and actual number of increments
			actual += combination.actual;
			total += combination.total;
		}
		
		// Just for fun: print the total and actual number of increments.
		System.out.println("Actual/total number of increments: " + actual + "/" + total);
		
		return new Automaton<L>(Q, P, T, q0);
	}

	/**
	 * Gets a global state from a list of local states.
	 * @param q			list of local states
	 * @return name of the global state.
	 */
	private static String compose(List<String> q) {
		String s = q.get(0);
		for (int i = 1; i < q.size(); i++) 
			s += "|" + q.get(i);
		return s;
	}
}
