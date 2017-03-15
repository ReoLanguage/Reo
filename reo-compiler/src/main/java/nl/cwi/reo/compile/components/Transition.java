package nl.cwi.reo.compile.components;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import nl.cwi.reo.interpret.ports.Port;

public final class Transition {

	/**
	 * Source state.
	 */
	private final Integer q1;

	/**
	 * Target state.
	 */
	private final Integer q2;

	/**
	 * Synchronization constraint.
	 */
	private final Set<Port> N;

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
	public Transition(Integer q1, Integer q2, Set<Port> N, Map<String, String> a) {
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
		this.N = Collections.unmodifiableSet(N);
		this.a = a;
	}

	/**
	 * Retrieves the source state of the current transition.
	 * 
	 * @return name of source state
	 */
	public Integer getSource() {
		return this.q1;
	}

	/**
	 * Retrieves the target state of the current transition.
	 * 
	 * @return name of target state
	 */
	public Integer getTarget() {
		return this.q2;
	}

	/**
	 * Retrieves the synchronization constraint of the current transition.
	 * 
	 * @return synchronization constraint
	 */
	public Set<Port> getSyncConstraint() {
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
}
