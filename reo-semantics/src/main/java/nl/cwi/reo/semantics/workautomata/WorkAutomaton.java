package nl.cwi.reo.semantics.workautomata;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
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

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.AutomatonSemantics;
import nl.cwi.reo.semantics.SemanticsType;
import nl.cwi.reo.util.Monitor;

import java.util.HashMap;

// TODO: Auto-generated Javadoc
/**
 * A work automaton.
 */
public final class WorkAutomaton implements AutomatonSemantics<WorkAutomaton> {

	/** Set of states. */
	private final Set<String> Q;

	/** Set of ports. */
	private final SortedSet<Port> P;

	/**
	 * Set of jobs.
	 */
	private final Set<String> J;

	/**
	 * Set of invariant conditions.
	 */
	private final Map<String, JobConstraint> I;

	/**
	 * List of outgoing transitions.
	 */
	private final Map<String, Set<Transition>> T;

	/**
	 * Initial state.
	 */
	private final String q0;

	/**
	 * Constructs a single state work automaton which is the identity with
	 * respect to composition.
	 */
	public WorkAutomaton() {
		this.Q = new HashSet<String>();
		this.P = new TreeSet<Port>();
		this.J = new HashSet<String>();
		this.I = new HashMap<String, JobConstraint>();
		this.T = new HashMap<String, Set<Transition>>();
		this.q0 = "q0";
		Q.add(q0);
		I.put(q0, new JobConstraint(true));
		T.put(q0, new TreeSet<Transition>());
	}

	/**
	 * Constructs a new work automaton.
	 * 
	 * @param Q
	 *            set of states
	 * @param P
	 *            set of ports
	 * @param J
	 *            set of jobs
	 * @param I
	 *            mapping from states to invariant conditions
	 * @param T
	 *            mapping from states to outgoing transitions
	 * @param q0
	 *            initial state
	 */
	public WorkAutomaton(Set<String> Q, SortedSet<Port> P, Set<String> J, Map<String, JobConstraint> I,
			Map<String, Set<Transition>> T, String q0) {
		this.Q = Q;
		this.P = P;
		this.J = J;
		this.I = I;
		this.T = T;
		this.q0 = q0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.semantics.Semantics#getNode(java.util.Set)
	 */
	@Override
	public WorkAutomaton getNode(Set<Port> node) {
		Set<String> Q = new HashSet<String>();
		SortedSet<Port> P = new TreeSet<Port>();
		Set<String> J = new HashSet<String>();
		Map<String, JobConstraint> I = new HashMap<String, JobConstraint>();
		Map<String, Set<Transition>> T = new HashMap<String, Set<Transition>>();
		String q0 = "q";
		Q.add(q0);
		I.put(q0, new JobConstraint(true));
		T.put(q0, new HashSet<Transition>());

		Set<Port> ins = new HashSet<Port>();
		Set<Port> outs = new HashSet<Port>();
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
			Transition t = new Transition(q0, q0, N, new JobConstraint(true));
			T.get(q0).add(t);
		}

		return new WorkAutomaton(Q, P, J, I, T, q0);
	}

	/**
	 * Gets the set of states of this work automaton.
	 *
	 * @return the states
	 */
	public Set<String> getStates() {
		return this.Q;
	}

	/**
	 * Gets the set of ports in the interface of this work automaton.
	 *
	 * @return the interface
	 */
	public SortedSet<Port> getInterface() {
		return this.P;
	}

	/**
	 * Gets the set of jobs of the work automaton.
	 *
	 * @return the jobs
	 */
	public Set<String> getJobs() {
		return this.J;
	}

	/**
	 * Gets the invariant condition of a state in the work automaton.
	 *
	 * @param q
	 *            state
	 * @return the invariant
	 */
	public @Nullable JobConstraint getInvariant(String q) {
		return this.I.get(q);
	}

	/**
	 * Gets the set of outgoing transitions of a state of the work automaton.
	 * 
	 * @param q
	 *            state
	 * @return set of outgoing transitions from the given state.
	 */
	public @Nullable Set<Transition> getTransitions(String q) {
		return this.T.get(q);
	}

	/**
	 * Gets the initial state of the work automaton.
	 *
	 * @return the initial
	 */
	public String getInitial() {
		return this.q0;
	}

	/**
	 * Connects the ports of the work automaton to nodes.
	 *
	 * @param links
	 *            relabeling function
	 * @return the work automaton
	 */
	public WorkAutomaton rename(Map<Port, Port> links) {

		// Initialize the Work Automaton fields.
		Set<String> Q = new HashSet<String>(this.Q);
		SortedSet<Port> P = new TreeSet<Port>();
		Set<String> J = new HashSet<String>(this.J);
		Map<String, JobConstraint> I = new HashMap<String, JobConstraint>(this.I);
		Map<String, Set<Transition>> T = new HashMap<String, Set<Transition>>();
		String q0 = this.q0;

		// Rename the ports in the interface
		for (Port a : this.P) {
			Port port = links.get(a);
			if (port == null)
				port = a;
			P.add(port);
		}

		// Add relabeled transitions to the set of transition
		for (Map.Entry<String, Set<Transition>> pair : this.T.entrySet()) {
			Set<Transition> outgoing = new TreeSet<Transition>();
			for (Transition t : pair.getValue())
				outgoing.add(t.rename(links));
			T.put(pair.getKey(), outgoing);
		}

		return new WorkAutomaton(Q, P, J, I, T, q0);
	}

	/**
	 * Gets the string representation of this work automaton in .dot format.
	 *
	 * @return the string
	 */
	public String toDOT() {
		StringBuilder str = new StringBuilder();

		str.append("// WorkAutomaton with interface " + this.P + " and jobs " + this.J + "\n");
		str.append("digraph {\n");
		for (String q : this.Q)
			str.append("\t" + q + " [label=\"" + q + "\"];\n");
		for (String q : this.Q) {
			Set<Transition> outq = this.T.get(q);
			if (outq != null) {
				for (Transition t : outq) {
					String q1 = t.getSource();
					String q2 = t.getTarget();
					String sc = t.getSyncConstraint().toString();
					String jc = t.getJobConstraint().toString();
					str.append("\t" + q1 + " -> " + q2 + " [label=\"" + sc + "," + jc + "\"];\n");
				}
			}
		}
		str.append("}");

		return str.toString();
	}

	/**
	 * Gets the string representation of this work automaton.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "(" + this.Q + "," + this.P + "," + this.J + "," + this.I + "," + this.T + "," + this.q0 + ")";
	}

	/**
	 * Produces a .dot file containing the work automaton.
	 * 
	 * @param A
	 *            work automaton
	 * @param fileName
	 *            the name of the file without .dot extension
	 * @return true if file is successfully written.
	 */
	public static boolean outputDOT(WorkAutomaton A, String fileName) {
		try {
			FileWriter out = new FileWriter(fileName + ".dot");
			out.write(A.toDOT());
			out.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	/**
	 * Computes the product of a list of work automata by means of a breadth
	 * first algorithm.
	 * 
	 * @param automata
	 *            a list of work automata
	 * @return the identity work automaton, if the list is empty; the first work
	 *         automaton, if the list is size 1; the product work automaton, if
	 *         the list contains at least 2 work automata.
	 */
	@Override
	public WorkAutomaton compose(List<WorkAutomaton> automata) {

		// Get the number of work automata.
		int size = automata.size();

		// If the product is empty, return the identity automaton.
		if (size == 0)
			return new WorkAutomaton();

		// If the product is trivial, return the unique work automaton.
		if (size == 1)
			return automata.get(0);

		// Initialize the work automaton fields.
		Set<String> Q = new HashSet<String>();
		SortedSet<Port> P = new TreeSet<Port>();
		Set<String> J = new HashSet<String>();
		Map<String, JobConstraint> I = new HashMap<>();
		Map<String, Set<Transition>> T = new HashMap<>();
		String q0;

		// Find the global initial state.
		List<String> s0 = new ArrayList<>();
		for (WorkAutomaton A : automata)
			s0.add(A.q0);
		q0 = composeState(s0);

		// Initialize the queue L of unexplored global states.
		Queue<List<String>> L = new LinkedList<>();

		// Add the initial state to the queue.
		L.add(s0);
		Q.add(q0);
		T.put(q0, new TreeSet<>());

		// Just for fun: count the actual and total number of steps to find
		// composable tuples of local transitions.
		int actual = 0;
		int total = 0;

		// Loop until the queue is empty.
		while (!L.isEmpty()) {

			// Get and remove the first unexplored global state in L.
			List<String> s1 = L.poll();

			if (s1 != null) {
				// Add global state q1 to the set of states Q
				Q.add(composeState(s1));

				// For each work automaton A.get(i), find the list
				// localtransitions.get(i) of all possible
				// local transitions from the local state s1.get(i).
				List<List<Transition>> localtransitions = new ArrayList<List<Transition>>();
				for (int i = 0; i < size; i++) {
					Set<Transition> x = automata.get(i).T.get(s1.get(i));
					if (x != null)
						localtransitions.add(new ArrayList<Transition>(x));
				}

				// Iterate over all *composable* combinations of local
				// transitions
				// from s1.
				TransitionIterator combination = new TransitionIterator(automata, s1, localtransitions);

				// Iterate over all combinations of local transitions.
				while (combination.hasNext()) {

					// Get the next composable combination of local transitions.
					List<Transition> tuple = combination.next();

					// Instantiate the synchronization constraint, job
					// constraint
					// and target state of the
					// global transition.
					SortedSet<Port> N = new TreeSet<Port>();
					SortedMap<String, Integer> w = new TreeMap<String, Integer>();
					Set<String> R = new HashSet<String>();
					List<String> s2 = new ArrayList<String>();

					for (int i = 0; i < size; i++) {
						// Add the ports in the synchronization constraint
						N.addAll(tuple.get(i).getSyncConstraint());

						// Add the job constraints and use the index as suffix
						// for
						// unique job names.
						for (Map.Entry<String, Integer> atom : tuple.get(i).getJobConstraint().getW().entrySet())
							w.put(atom.getKey() + i, atom.getValue());

						// Add the required jobs with index suffix.
						for (String job : tuple.get(i).getJobConstraint().getR())
							R.add(job + i);

						// Add the target state
						s2.add(tuple.get(i).getTarget());
					}

					// Construct the source and target state and job constraint.
					String q1 = composeState(s1);
					String q2 = composeState(s2);
					JobConstraint jc = new JobConstraint(w, R);

					// Construct the global transition.
					Transition t = new Transition(q1, q2, N, jc);

					// Add the target state to the work automaton, and add it to
					// the
					// queue if its new.
					if (!Q.add(q2))
						L.add(s2);

					// Add the global transition to the work automaton
					Set<Transition> outq1 = T.get(q1);
					if (outq1 != null)
						outq1.add(t);
				}

				// Just for fun: add the total and actual number of increments
				actual += combination.actual;
				total += combination.total;
			}
		}

		// Just for fun: print the total and actual number of increments.
		System.out.println("Actual/total number of increments: " + actual + "/" + total);

		return new WorkAutomaton(Q, P, J, I, T, q0);
	}

	/**
	 * Gets a global state from a list of local states.
	 * 
	 * @param q
	 *            list of local states
	 * @return name of the global state.
	 */
	private String composeState(List<String> q) {
		String s = q.get(0);
		for (int i = 1; i < q.size(); i++)
			s += "|" + q.get(i);
		return s;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.Expression#evaluate(nl.cwi.reo.interpret.Scope,
	 * nl.cwi.reo.util.Monitor)
	 */
	@Override
	public WorkAutomaton evaluate(Scope s, Monitor m) {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.semantics.Semantics#restrict(java.util.Collection)
	 */
	@Override
	public WorkAutomaton restrict(Collection<? extends Port> intface) {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.semantics.Semantics#getType()
	 */
	@Override
	public SemanticsType getType() {
		return SemanticsType.WA;
	}
}
