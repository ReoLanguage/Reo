package nl.cwi.reo.automata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * A transition, labeled with a generic label of type L, that is used in a {@link nl.cwi.reo.automata.Automaton}.
 * If the transition label type L is immutable, then Transition<L> is immutable too.
 * @param L 	transition label type
 */
public class Transition<L extends Label<L>> implements Comparable<Transition<L>> {

	/**
	 * Source state.
	 */
	private final State q1; 
	
	/**
	 * Target state.
	 */
	private final State q2; 

	/**
	 * Synchronization constraint.
	 */
	private final SortedSet<String> N;

	/**
	 * Transition label.
	 */
	private final L lbl;
	
	/**
	 * Constructs a silent self loop transition at state q.
	 * @param q 	state
	 */
	public Transition(State q) {
		this.q1 = q;
		this.q2 = q;
		this.N = new TreeSet<String>();
		this.lbl = null;	
	}
	
	/**
	 * Constructs a new transition.
	 * 
	 * @param q1		source state
	 * @param q2		target state
	 * @param N			synchronization constraint
	 * @param lbl		transition label
	 */
	public Transition(State q1, State q2, SortedSet<String> N, L lbl) {
		if (q1 == null)
			throw new IllegalArgumentException("No source state specified.");	
		if (q2 == null)
			throw new IllegalArgumentException("No target state specified.");
		if (N == null)
			throw new IllegalArgumentException("No synchronization constraint specified.");
		if (lbl == null)
			throw new IllegalArgumentException("No transition label specified.");
		this.q1 = q1;
		this.q2 = q2;
		this.N = Collections.unmodifiableSortedSet(N);
		this.lbl = lbl;
	}
	
	/**
	 * Constructs a copy of this transition.
	 * @param t		original transition.
	 */
	public Transition(Transition<? extends L> t) {
		this.q1 = t.q1;
		this.q2 = t.q2;
		this.N = t.N;
		this.lbl = t.lbl;
		
	}
	
	/**
	 * Retrieves the source state of the current transition.
	 * @return name of source state
	 */
	public State getSource() {
		return this.q1;
	}
	
	/**
	 * Retrieves the target state of the current transition.
	 * @return name of target state
	 */
	public State getTarget() {
		return this.q2;
	}
	
	/**
	 * Retrieves the synchronization constraint of the current transition.
	 * @return synchronization constraint
	 */
	public SortedSet<String> getSyncConstraint() {
		return this.N;
	}
	
	/**
	 * Retrieves the job constraint of the current transition.
	 * @return job constraint
	 */
	public L getLabel() {
		return this.lbl;
	}

	/**
	 * Composes this transition with of a list of transitions.
	 * @param automata		a list of work automata
	 * @return Composed transition.
	 */
	public Transition<L> compose(List<Transition<L>> transitions) {
		List<State> sources = new ArrayList<State>();
		List<State> targets = new ArrayList<State>();
		TreeSet<String> syncs = new TreeSet<String>();
		List<L> labels = new ArrayList<L>();
		for (Transition<L> t : transitions) {
			sources.add(t.q1);
			targets.add(t.q2);
			syncs.addAll(t.N);
			labels.add(t.getLabel());
		}
		State q1 = this.q1.compose(sources);
		State q2 = this.q2.compose(targets);
		SortedSet<String> N = new TreeSet<String>(this.N);
		N.addAll(syncs);
		L lbl = this.lbl == null ? null : this.lbl.compose(labels);
		
		return new Transition<L>(q1, q2, N, lbl);
	}

	/**
	 * Restricts the interface of this transition
	 * @param intface			smaller interface
	 * @returns Transition with interface intface.
	 */
	public Transition<L> restrict(Set<String> intface) {
		SortedSet<String> N = new TreeSet<String>(this.N);
		N.retainAll(intface);		
		L lbl = this.lbl == null ? null : this.lbl.restrict(intface);
		return new Transition<L>(this.q1, this.q2, N, lbl);
	}

	/**
	 * Renames entry.Key() to entry.Value() for every entry renaming map.
	 * @param links		renaming map
	 * @return renamed transition.
	 */
	public Transition<L> rename(Map<String, String> links) {
		SortedSet<String> rN = new TreeSet<String>();
		for (String port : this.N) {
			String newport;
			if ((newport = links.get(port)) == null)
				newport = port;
			rN.add(newport);
		}
		L lbl = this.lbl == null ? null : this.lbl.rename(links);
		return new Transition<L>(this.q1, this.q2, rN, lbl);
	}
	
	/**
	 * Returns the string representation of this transition. Ports in the synchronization 
	 * constraint are ordered lexicographically.
	 */
	@Override 
	public String toString() { 
		return q1 + " -- " + N + ", " + lbl + " -> " + q2;
	}

	/**
	 * Returns the hash code of the string representation of this transition.
	 */
	@Override 
	public int hashCode() {
		return this.toString().hashCode();
	}
	
	/**
	 * Compares this transition with the specified transition for order.
	 * @param other 	the transition to be compared.
	 * @return a negative integer, zero, or a positive integer as this transition is less than, equal to, or greater than the specified transition.
	 */
	public int compareTo(Transition<L> other) {
		return this.toString().compareTo(other.toString());
	}

	/**
	 * Compares this transition to the specified object. The result is true if and only if the argument 
	 * is not null and is a Transition object that represents the same transition as this object.
	 * @param other 	the transition to be compared.
	 * @return true if the given object represents a Transition equivalent to this transition, false otherwise.
	 */
	@Override 
	public boolean equals(Object other) {
		if (!(other instanceof Transition<?>)) return false;
		return ((Transition<?>)other).toString().equals(this.toString());
	}
}
