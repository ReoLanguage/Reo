package nl.cwi.reo.automata;

import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * A transition, labeled with a generic label of type L, that is used in a {@link nl.cwi.reo.automata.Automaton}.
 */
public final class Transition<L extends Label<L>> implements Comparable<Transition<L>> {

	/**
	 * Source state.
	 */
	private final String q1; 
	
	/**
	 * Target state.
	 */
	private final String q2; 

	/**
	 * Synchronization constraint.
	 */
	private final SortedSet<String> N;

	/**
	 * Transition label.
	 */
	private final L lbl;
	
	/**
	 * Constructs a new transition.
	 * 
	 * @param q1		name of the source state
	 * @param q2		name of the target state
	 * @param N			synchronization constraint
	 * @param lbl		transition label
	 */
	public Transition(String q1, String q2, SortedSet<String> N, L lbl) {
		this.q1 = q1;
		this.q2 = q2;
		this.N = N;
		this.lbl = lbl;
	}
	
	/**
	 * Retrieves the source state of the current transition.
	 * @return name of source state
	 */
	public String getSource() {
		return this.q1;
	}
	
	/**
	 * Retrieves the target state of the current transition.
	 * @return name of target state
	 */
	public String getTarget() {
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
	 * Renames the ports in the synchronization constraint.
	 * @param r		renaming map
	 * @return transition with renamed synchronization constraint.
	 */
	public Transition<L> rename(Map<String, String> r) {
		SortedSet<String> rN = new TreeSet<String>(this.N);
		for (String port : this.N) {
			String newport;
			if ((newport = r.get(port)) == null)
				newport = port;
			rN.add(newport);
		}
		return new Transition<L>(this.q1, this.q2, rN, this.lbl);
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
