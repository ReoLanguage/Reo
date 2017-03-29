package nl.cwi.reo.compile.components;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.symbolicautomata.Term;
import nl.cwi.reo.semantics.symbolicautomata.Variable;

public final class TransitionRule {

	/**
	 * Synchronization constraint.
	 */
	private final Set<Port> N;

	/**
	 * Update (assigns value to memory cells and output ports)
	 */
	private final Map<Port, Term> a;

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
	public TransitionRule(Set<Port> N, Map<Port, Term> a) {
		if (N == null)
			throw new IllegalArgumentException("No synchronization constraint specified.");
		if (a == null)
			throw new IllegalArgumentException("No transition label specified.");
		this.N = Collections.unmodifiableSet(N);
		this.a = a;
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
	public Map<Port, Term> getAction() {		
		return this.a;
	}
}
