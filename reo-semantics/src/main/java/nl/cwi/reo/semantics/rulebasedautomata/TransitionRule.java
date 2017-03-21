package nl.cwi.reo.semantics.rulebasedautomata;

import java.util.Map;

import nl.cwi.reo.interpret.ports.Port;

public class TransitionRule {

	/**
	 * Source state: q[k] = null means that this transition is independent of
	 * q[k].
	 */
	private final Constant[] q0;

	/**
	 * Target state.
	 */
	private final DataTerm[] q1;

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
	 * @param q0
	 *            source state
	 * @param q1
	 *            target state
	 * @param N
	 *            synchronization constraint
	 * @param g
	 *            data constraint
	 */
	public TransitionRule(Constant[] q0, DataTerm[] q1, SyncConstraint N, DataConstraint g) {
		if (q0.length != q1.length)
			throw new IllegalArgumentException();
		this.q0 = q0;
		this.q1 = q1;
		this.N = N;
		this.g = g;
	}

	public int getDimension() {
		return q0.length;
	}

	public Object[] getSource() {
		return q0;
	}

	public DataTerm[] getTarget() {
		return q1;
	}

	public SyncConstraint getSyncConstraint() {
		return N;
	}

	public DataConstraint getDataConstraint() {
		return g;
	}

	public TransitionRule rename(Map<Port, Port> links) {

		DataTerm[] q1 = new DataTerm[this.q1.length];
		for (int k = 0; k < this.q1.length; k++)
			q1[k] = this.q1[k].rename(links);

		return new TransitionRule(q0, q1, N.rename(links), g.rename(links));
	}
}
