package nl.cwi.reo.compile.components;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.SortedSet;

import nl.cwi.reo.interpret.ports.Port;

public final class Transition {

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
	private final SortedSet<Port> N;

	/**
	 * Transition label.
	 */
	private final Map<String, String> a;

	/**
	 * Constructs a new transition.
	 * 
	 * @param q1
	 *            source state
	 * @param q2
	 *            target state
	 * @param N
	 *            synchronization constraint
	 * @param a
	 *            transition label
	 */
	public Transition(String q1, String q2, SortedSet<Port> N, Map<String, String> a) {
		if (q1 == null)
			throw new IllegalArgumentException("No source state specified.");
		if (q2 == null)
			throw new IllegalArgumentException("No target state specified.");
		if (N == null)
			throw new IllegalArgumentException("No synchronization constraint specified.");
		if (a == null)
			throw new IllegalArgumentException("No transition label specified.");
		this.q1 = q1;
		this.q2 = q2;
		this.N = Collections.unmodifiableSortedSet(N);
		this.a = a;
	}

	/**
	 * Retrieves the source state of the current transition.
	 * 
	 * @return name of source state
	 */
	public String getSource() {
		return this.q1;
	}

	/**
	 * Retrieves the target state of the current transition.
	 * 
	 * @return name of target state
	 */
	public String getTarget() {
		return this.q2;
	}

	/**
	 * Retrieves the synchronization constraint of the current transition.
	 * 
	 * @return synchronization constraint
	 */
	public SortedSet<Port> getSyncConstraint() {
		return this.N;
	}

	/**
	 * Retrieves the job constraint of the current transition.
	 * 
	 * @return job constraint
	 */
	public Map<String, String> getAction() {		
		return this.a;
	}

	@Override
	public String toString() {
		return "(" + q1 + "," + N + "," + a + "," + q2 + ")";
	}

	@Override
	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (other == this)
			return true;
		if (!(other instanceof Transition))
			return false;
		Transition p = (Transition) other;
		return Objects.equals(this.q1, p.q1) && Objects.equals(this.q2, p.q2) && Objects.equals(this.N, p.N)
				&& Objects.equals(this.a, p.a);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.q1, this.q2, this.N, this.a);
	}
}
