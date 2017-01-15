package nl.cwi.reo.workautomata;

import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import nl.cwi.reo.semantics.Port;

/**
 * A work automaton transition that is used in a {@link nl.cwi.reo.workautomata.WorkAutomaton}.
 */
public final class Transition implements Comparable<Transition> {

	/**
	 * Source state of the transition.
	 */
	private final String q1; 
	
	/**
	 * Target state of the transition.
	 */
	private final String q2; 

	/**
	 * Synchronization constraint of the transition.
	 */
	private final SortedSet<Port> N;

	/**
	 * Job constraint of the transition.
	 */
	private final JobConstraint jc;
	
	/**
	 * Constructs a new transition.
	 * 
	 * @param q1		name of the source state
	 * @param q2		name of the target state
	 * @param N			synchronization constraint
	 * @param jc		job constraint
	 */
	public Transition(String q1, String q2, SortedSet<Port> N, JobConstraint jc) {
		this.q1 = q1;
		this.q2 = q2;
		this.N = N;
		this.jc = jc;
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
	public SortedSet<Port> getSyncConstraint() {
		return this.N;
	}
	
	/**
	 * Retrieves the job constraint of the current transition.
	 * @return job constraint
	 */
	public JobConstraint getJobConstraint() {
		return this.jc;
	}
	
	/**
	 * Returns a silent self loop transition at the given state.
	 * @param q		name of the state.
	 * @return new silent self loop transition
	 */
	public static Transition getIdlingTransition(String q) {
		SortedSet<Port> N = new TreeSet<Port>();
		JobConstraint jc = new JobConstraint(true);
		return new Transition(q, q, N, jc);
	}
	
	/**
	 * Renames the ports in the synchronization constraint.
	 * @param r		renaming map
	 * @return new transition with renamed synchronization constraint.
	 */
	public Transition rename(Map<Port, Port> r) {
		SortedSet<Port> rN = new TreeSet<Port>();
		for (Port port : this.N) {
			Port newport;
			if ((newport = r.get(port)) == null)
				newport = port;
			rN.add(newport);
		}
		return new Transition(this.q1, this.q2, rN, this.jc);
	}
	
	/**
	 * Returns the string representation of this transition. Ports in the synchronization 
	 * constraint are ordered lexicographically.
	 */
	@Override 
	public String toString() { 
		return "(" + q1 + "," + q2 + "," + N + "," + jc + ")";
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
	public int compareTo(Transition other) {
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
		if (!(other instanceof Transition)) return false;
		Transition t = (Transition)other;
		return t.toString().equals(this.toString());
	}
}
