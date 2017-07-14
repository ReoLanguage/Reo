package nl.cwi.reo.semantics.automata;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.util.Monitor;

import java.util.HashMap;
import java.util.HashSet;

/**
 * A port automaton in which each transition has, besides a synchronization
 * constraint, a generic label of type L. If the transition label type L is
 * immutable, then Automaton<L> is immutable too.
 */
public class Automaton<L extends Label<L>> {

	/**
	 * Set of states
	 */
	protected final SortedSet<State> states;

	/**
	 * Set of ports
	 */
	protected final SortedSet<Port> iface;

	/**
	 * List of outgoing transitions.
	 */
	protected final Map<State, Set<Transition<L>>> out;

	/**
	 * Initial state.
	 */
	protected final State initial;

	/**
	 * Instance of a label.
	 */
	protected final L lbl;

	/**
	 * Constructs the identity automaton with respect to composition, which is a
	 * single state automaton with empty interface and no transitions.
	 * 
	 * @param type
	 *            type of semantics
	 * @param lbl
	 *            instance of a label
	 */
	public Automaton(L lbl) {
		this.initial = new State("*");

		SortedSet<State> states = new TreeSet<State>();
		states.add(initial);
		this.states = Collections.unmodifiableSortedSet(states);

		this.iface = Collections.unmodifiableSortedSet(new TreeSet<Port>());

		Map<State, Set<Transition<L>>> out = new HashMap<State, Set<Transition<L>>>();
		out.put(initial, Collections.unmodifiableSet(new HashSet<Transition<L>>()));
		this.out = Collections.unmodifiableMap(out);

		this.lbl = lbl;
	}

	/**
	 * Constructs a new work automaton. If the initial state is not present in
	 * the set of states, then it is added to the set of states.
	 * 
	 * @param Q
	 *            set of states
	 * @param P
	 *            set of ports
	 * @param T
	 *            mapping from states to outgoing transitions
	 * @param q0
	 *            initial state
	 * @param type
	 *            type of semantics
	 * @param lbl
	 *            instance of a label
	 */
	public Automaton(SortedSet<State> Q, SortedSet<Port> P, Map<State, Set<Transition<L>>> T, State q0, L lbl) {
		if (Q == null)
			throw new NullPointerException("No set of states specified.");
		if (P == null)
			throw new NullPointerException("No interface specified.");
		for (State q : Q)
			if (T.get(q) == null)
				throw new NullPointerException("State " + q + " does not have outgoing transitions.");
		if (q0 == null)
			throw new NullPointerException("No initial state specified.");

		this.initial = q0;

		SortedSet<State> states = new TreeSet<State>(Q);
		states.add(initial);
		this.states = Collections.unmodifiableSortedSet(states);

		this.iface = Collections.unmodifiableSortedSet(new TreeSet<Port>(P));

		Map<State, Set<Transition<L>>> out = new HashMap<State, Set<Transition<L>>>();
		for (Map.Entry<State, Set<Transition<L>>> entry : T.entrySet())
			out.put(entry.getKey(), Collections.unmodifiableSet(new HashSet<Transition<L>>(entry.getValue())));
		if (out.get(initial) == null)
			out.put(initial, Collections.unmodifiableSet(new HashSet<Transition<L>>()));
		this.out = Collections.unmodifiableMap(out);

		this.lbl = lbl;
	}

	/**
	 * Gets the set of states of this automaton.
	 * 
	 * @return set of states
	 */
	public SortedSet<State> getStates() {
		return this.states;
	}

	/**
	 * Gets the interface of this automaton.
	 * 
	 * @return set of names.
	 */
	public SortedSet<Port> getInterface() {
		return this.iface;
	}

	/**
	 * Gets the outgoing transitions from a given state.
	 * 
	 * @return list of outgoing transitions.
	 */
	public Map<State, Set<Transition<L>>> getTransitions() {
		return this.out;
	}

	/**
	 * Gets the initial state of this automaton.
	 * 
	 * @return initial state.
	 */
	public State getInitial() {
		return this.initial;
	}

	/**
	 * Returns a copy of this automaton with given initial state.
	 * 
	 * @param q0
	 *            new initial state.
	 */
	public Automaton<L> setInitial(State q0) {
		return new Automaton<L>(states, iface, out, q0, lbl);
	}

	/**
	 * Compose this automaton with an array of automata by means of a breadth
	 * first algorithm.
	 * 
	 * @param automata
	 *            a array of automata
	 * @return this automaton, if the array is empty, and the product automaton,
	 *         otherwise.
	 */
	public Automaton<L> compose(Iterable<? extends Automaton<L>> automata) {

		// Initialize the automaton fields.
		SortedSet<State> Q = new TreeSet<State>();
		SortedSet<Port> P = new TreeSet<Port>();
		Map<State, Set<Transition<L>>> T = new HashMap<State, Set<Transition<L>>>();
		State q0;

		// Find the global initial state.
		List<State> qi0 = new ArrayList<State>();
		for (Automaton<L> Ai : automata) {
			qi0.add(Ai.initial);
			P.addAll(Ai.iface);
		}
		q0 = this.initial.compose(qi0);

		// Initialize the queue L of unexplored global states.
		Queue<Pair<State, List<State>>> L = new LinkedList<Pair<State, List<State>>>();

		// Add the initial state to the queue.
		L.add(Pair.create(this.initial, qi0));
		Q.add(q0);
		T.put(q0, new HashSet<Transition<L>>());

		// Loop until the queue is empty.
		while (!L.isEmpty()) {

			// Get and remove the first unexplored global state in L.
			Pair<State, List<State>> s1 = L.poll();
			if (s1 != null) {
				State q1 = s1.first.compose(s1.second);

				// Add global state s1 to the set of states Q
				Q.add(q1);

				// Iterate over all *composable* combinations of local
				// transitions from s1.
				// TODO this.setInitial(q) unnecessarily copies the whole
				// automaton.
				List<Automaton<L>> As = new ArrayList<Automaton<L>>();
				As.add(this.setInitial(s1.first));
				Iterator<? extends Automaton<L>> Ai = automata.iterator();
				Iterator<State> si = s1.second.iterator();
				while (Ai.hasNext() && si.hasNext())
					As.add(Ai.next().setInitial(si.next()));
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

					// Construct the target state.
					State q02 = t0.getTarget();
					List<State> qi2 = new ArrayList<State>();
					for (Transition<L> tr : ti)
						qi2.add(tr.getTarget());
					Pair<State, List<State>> s2 = Pair.create(q02, qi2);
					State q2 = s2.first.compose(s2.second);

					// Add the target state to the work automaton, and add it to
					// the queue if its new.
					if (Q.add(q2)) {
						T.put(q2, new HashSet<Transition<L>>());
						L.add(s2);
					}

					// Add the global transition to the work automaton
					Set<Transition<L>> outq1 = T.get(q1);
					if (outq1 != null)
						outq1.add(t);
				}
			}
		}

		return new Automaton<L>(Q, P, T, q0, lbl);
	}

	/**
	 * Restricts the interface of this automaton
	 * 
	 * @param intface
	 *            smaller interface
	 * @returns Automaton with interface intface.
	 */
	public Automaton<L> restrict(Collection<? extends Port> intface) {
		SortedSet<Port> iface = new TreeSet<Port>(intface);
		Map<State, Set<Transition<L>>> out = new HashMap<State, Set<Transition<L>>>();
		for (Map.Entry<State, Set<Transition<L>>> entry : this.out.entrySet()) {
			Set<Transition<L>> outq = new HashSet<Transition<L>>();
			for (Transition<L> t : entry.getValue())
				outq.add(t.restrict(intface));
			out.put(entry.getKey(), outq);
		}
		return new Automaton<L>(states, iface, out, initial, lbl);
	}

	/**
	 * Relabels a name x to a name y, for every key-value pair (x,y) in r.
	 * 
	 * @param links
	 *            relabeling function
	 */
	public Automaton<L> rename(Map<Port, Port> links) {

		// Initialize the automaton
		SortedSet<State> Q = this.states;
		SortedSet<Port> P = new TreeSet<Port>();
		Map<State, Set<Transition<L>>> T = new HashMap<State, Set<Transition<L>>>();

		// Rename the ports in the interface
		for (Port a : this.iface) {
			Port b = links.get(a);
			if (b == null)
				b = a;
			P.add(b);// P.add(new Port(b.getName()));
		}

		// Add relabeled transitions to the set of transition
		for (Map.Entry<State, Set<Transition<L>>> entry : this.out.entrySet()) {
			Set<Transition<L>> outq = new HashSet<Transition<L>>();
			for (Transition<L> t : entry.getValue())
				outq.add(t.rename(links));
			T.put(entry.getKey(), outq);
		}

		return new Automaton<L>(Q, P, T, initial, lbl);
	}

	/**
	 * Gets the string representation of this automaton in .dot format.
	 */
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();

		str.append("Interface " + iface + "; initial state " + initial + "\n");
		for (State q : this.states) {
			Set<Transition<L>> outq = this.out.get(q);
			if (outq != null) {
				for (Transition<L> t : outq) {
					State q1 = t.getSource();
					State q2 = t.getTarget();
					String sc = t.getSyncConstraint().toString();
					L lb = t.getLabel();
					str.append("\t" + q1 + " -> " + q2 + " : " + sc + ", " + lb + "\n");
				}
			}
		}

		return str.toString();
	}

	/**
	 * Produces a .dot file containing this work automaton.
	 * 
	 * @param A
	 *            work automaton
	 * @param fileName
	 *            the name of the file without .dot extension
	 * @return true if file is successfully written.
	 */
	public boolean toDOTFile(String fileName) {
		try {
			FileWriter out = new FileWriter(fileName + ".dot");
			out.write("// Automaton with interface " + this.iface + " and initial state " + this.initial + "\n");
			out.write("digraph {\n");
			for (State q : this.states)
				out.write("\t" + q + " [label=\"" + q + "\"];\n");
			for (State q : this.states) {
				Set<Transition<L>> outq = this.out.get(q);
				if (outq != null) {
					for (Transition<L> t : outq) {
						State q1 = t.getSource();
						State q2 = t.getTarget();
						String sc = t.getSyncConstraint().toString();
						L lb = t.getLabel();
						out.write("\t" + q1 + " -> " + q2 + " [label=\"" + sc + ", " + lb + "\"];\n");
					}
				}
			}
			out.write("}");
			out.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	public Automaton<L> getNode(Set<Port> node) {
		SortedSet<State> Q = new TreeSet<State>();
		SortedSet<Port> P = new TreeSet<Port>();
		Map<State, Set<Transition<L>>> T = new HashMap<State, Set<Transition<L>>>();
		State q0 = new State("q");
		Q.add(q0);
		T.put(q0, new HashSet<Transition<L>>());

		SortedSet<Port> ins = new TreeSet<Port>();
		SortedSet<Port> outs = new TreeSet<Port>();
		for (Port p : node) {
			P.add(p);
			switch (p.getType()) {
			case IN:
				outs.add(p);
				break;
			case OUT:
				ins.add(p);
				break;
			default:
				break;
			}
		}

		for (Port p : ins) {
			SortedSet<Port> N = new TreeSet<Port>(outs);
			N.add(p);
			Transition<L> t = new Transition<L>(q0, q0, N, lbl.getLabel(N));
			T.get(q0).add(t);
		}

		return new Automaton<L>(Q, P, T, q0, lbl);
	}

	public Automaton<L> evaluate(Scope s, Monitor m) {
		Map<State, Set<Transition<L>>> out = new HashMap<State, Set<Transition<L>>>();
		for (Map.Entry<State, Set<Transition<L>>> entry : this.out.entrySet()) {
			Set<Transition<L>> outq = new HashSet<Transition<L>>();
			for (Transition<L> t : entry.getValue())
				outq.add(t.evaluate(s, m));
			out.put(entry.getKey(), outq);
		}
		return new Automaton<L>(states, iface, out, initial, lbl);
	}
}
