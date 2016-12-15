package nl.cwi.reo.automata;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.HashMap;

/**
 * A port automaton in which each transition has, besides a synchronization constraint, 
 * a generic label of type L. If the transition label type L is immutable, then Automaton<L> 
 * is immutable too.
 */
public  class Automaton<L extends Label<L>> implements Semantics<Automaton<L>> {

	/**
	 * Set of states
	 */
	public final Set<State> states;

	/**
	 * Set of ports
	 */
	public final Set<String> iface;

	/**
	 * List of outgoing transitions.
	 */
	public final Map<State, List<Transition<L>>> outgoingTransitions;

	/**
	 * Initial state.
	 */
	public final State initial;

	/**
	 * Constructs the identity automaton with respect to composition, which
	 * is a single state automaton without transitions.
	 */
	public Automaton() {
		this.states = new HashSet<State>();
		this.iface = new HashSet<String>();
		this.outgoingTransitions = new HashMap<State, List<Transition<L>>>();
		this.initial = new State("q0");
		states.add(initial);
		outgoingTransitions.put(initial, new ArrayList<Transition<L>>());
	}
	
	/**
	 * Constructs a new work automaton.
	 * @param Q		set of states
	 * @param P		set of ports
	 * @param T		mapping from states to outgoing transitions
	 * @param q0 	initial state
	 */
	public Automaton(Set<State> Q, Set<String> P, Map<State, List<Transition<L>>> T, State q0) {
		this.states = Collections.unmodifiableSet(new HashSet<State>(Q));
		this.iface = Collections.unmodifiableSet(new HashSet<String>(P));
		Map<State, List<Transition<L>>> out = new HashMap<State, List<Transition<L>>>();
		for (Map.Entry<State, List<Transition<L>>> entry : T.entrySet())
			out.put(entry.getKey(), new ArrayList<Transition<L>>(entry.getValue()));
		this.outgoingTransitions = Collections.unmodifiableMap(out);
		this.initial = q0;
	}
	
	/**
	 * Constructs a copy of an automaton.
	 * @param A		original automaton.
	 */
	public Automaton(Automaton<L> A) {
		this.states = A.states;
		this.iface = A.iface;
		this.outgoingTransitions = A.outgoingTransitions;
		this.initial = A.initial;
	}
	
	/**
	 * Constructs a copy of an automaton, with given initial state.
	 * @param A		original automaton.
	 */
	public Automaton(Automaton<L> A, State q0) {
		this.states = A.states;
		this.iface = A.iface;
		this.outgoingTransitions = A.outgoingTransitions;
		this.initial = q0;
	}

	/**
	 * Compose this automaton with a list of automata by means of
	 * a breadth first algorithm.
	 * @param automata		a list of work automata
	 * @return this automaton, if the list is empty, and the product 
	 * automaton, otherwise.
	 */
	public Automaton<L> compose(List<Automaton<L>> automata) {
		
		// Get the number of work automata.
		int size = automata.size();
		
		// If the product is empty, return the identity automaton.
		if (size == 0) 
			return this;

		// Initialize the automaton fields.
		Set<State> Q = new HashSet<State>();
		Set<String> P = new HashSet<String>();
		Map<State, List<Transition<L>>> T = new HashMap<State, List<Transition<L>>>();
		State q0;

		// Find the global initial state.
		List<State> qi0 = new ArrayList<State>();
		for (int i = 0; i < size; i++) 
			qi0.add(automata.get(i).initial);
		q0 = this.initial.compose(qi0);
		
		// Initialize the queue L of unexplored global states.
		Queue<Pair<State,List<State>>> L = new LinkedList<Pair<State,List<State>>>();
		
		// Add the initial state to the queue.
		L.add(Pair.create(q0, qi0));
		Q.add(q0);
		T.put(q0, new ArrayList<Transition<L>>());
		
		// Loop until the queue is empty.
		while (!L.isEmpty()) {
			
			// Get and remove the first unexplored global state in L.
			Pair<State,List<State>> s1 = L.poll();
			State q1 = s1.first.compose(s1.second);
			
			// Add global state s1 to the set of states Q
			Q.add(q1);

			// Iterate over all *composable* combinations of local transitions from s1.
			List<Automaton<L>> As = new ArrayList<Automaton<L>>();
			As.add(new Automaton<L>(this, s1.first));
			for (int i = 0; i < automata.size(); ++i) 
				As.add(new Automaton<L>(automata.get(i), s1.second.get(i)));
			TransitionIterator<L> combination = new TransitionIterator<L>(As);

			// Iterate over all combinations of local transitions.
			while (combination.hasNext()) {
				
				// Get the next composable combination of local transitions.
				List<Transition<L>> tuple = combination.next();
				
				// Construct the global transition.
				Transition<L> t0 = tuple.get(0);
				List<Transition<L>> ti = new ArrayList<Transition<L>>(tuple);
				ti.remove(0);
				Transition<L> t = t0.compose(ti);

				// Add the target state to the work automaton, and add it to the queue if its new.
				if (!Q.add(t.getTarget())) {
					List<State> qi2 = new ArrayList<State>();
					for (Transition<L> tr : ti)
						qi2.add(tr.getTarget());
					L.add(Pair.create(t0.getTarget(), qi2));
				}
				
				// Add the global transition to the work automaton
				T.get(q1).add(t);				
			}
		}
		
		return new Automaton<L>(Q, P, T, q0);
	}

	/**
	 * Restricts the interface of this automaton
	 * @param intface			smaller interface
	 * @returns Automaton with interface intface.
	 */
	public Automaton<L> restrict(Set<String> intface) {
		Map<State, List<Transition<L>>> out = new HashMap<State, List<Transition<L>>>();
		for (Map.Entry<State, List<Transition<L>>> entry : this.outgoingTransitions.entrySet()) {
			List<Transition<L>> outq = new ArrayList<Transition<L>>();
			for (Transition<L> t : entry.getValue()) 
				outq.add(t.restrict(intface));
			out.put(entry.getKey(), outq);		
		}
		return new Automaton<L>(this.states, intface, out, this.initial);
	}

	/**
	 * Relabels a name x to a name y, for every key-value pair (x,y) in r.
	 * @param links		relabeling function
	 */
	public Automaton<L> rename(Map<String, String> links) {
		
		// Initialize the automaton
		Set<State> Q = this.states;
		Set<String> P = new HashSet<String>(this.iface);
		Map<State, List<Transition<L>>> T = new HashMap<State, List<Transition<L>>>();
		
		// Rename the ports in the interface
		for (String a : this.iface) {
			String port;
			if ((port = links.get(a)) == null)
				port = a;
			P.add(port);
		}
		
		// Add relabeled transitions to the set of transition
		for (List<Transition<L>> outgoing : this.outgoingTransitions.values())
			for (Transition<L> t : outgoing) 
				T.get(t.getSource()).add(t.rename(links));
		
		return new Automaton<L>(Q, P, T, this.initial);
	}
	
	/**
	 * Gets the string representation of this automaton in .dot format.
	 */
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		 
		str.append("// Automaton with interface " + this.iface + "\n");
		str.append("digraph {\n");
		for (State q : this.states) 
			str.append("\t" + q + " [label=\"" + q + "\"];\n");
		for (State q : this.states) {
			for (Transition<L> t : this.outgoingTransitions.get(q)) {
				String q1 = t.getSource().toString();
				String q2 = t.getTarget().toString();
				String sc = t.getSyncConstraint().toString();
				str.append("\t" + q1 + " -> "+ q2 + " [label=\"" + sc + "\"];\n");
			}
		}
		str.append("}");
		 
		return str.toString();
	}
	
	/**
	 * Produces a .dot file containing this work automaton.
	 * @param A				work automaton
	 * @param fileName		the name of the file without .dot extension
	 * @return true if file is successfully written.
	 */
	public boolean toFile(String fileName) {
		try {
			FileWriter out = new FileWriter(fileName + ".dot");
			out.write(this.toString());
			out.close();
		} catch (IOException e) {
			return false;
		} 
		return true;
	}
}
