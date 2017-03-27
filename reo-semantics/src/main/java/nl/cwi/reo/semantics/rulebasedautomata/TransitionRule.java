package nl.cwi.reo.semantics.rulebasedautomata;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import nl.cwi.reo.interpret.ports.Port;

public class TransitionRule {

	/**
	 * Synchronization constraint.
	 */
	private final SyncConstraint N;

	/**
	 * Data constraint.
	 */
	private final DataConstraint g;

	/**
	 * Constructs a new transition rule.
	 * 
	 * @param N
	 *            synchronization constraint
	 * @param g
	 *            data constraint
	 */
	public TransitionRule(SyncConstraint N, DataConstraint g) {
		this.N = N;
		this.g = g;
	}

	public SyncConstraint getN() {
		return N;
	}

	public DataConstraint getG() {
		return g;
	}

	public TransitionRule rename(Map<Port, Port> links) {
		return new TransitionRule(N.rename(links), g.rename(links));
	}

	public TransitionRule compose(List<TransitionRule> components) {
		return null;
	}

	public TransitionRule restrict(Collection<? extends Port> intface) {
		return null;	
	}
}
