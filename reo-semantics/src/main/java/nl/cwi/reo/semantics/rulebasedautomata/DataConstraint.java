package nl.cwi.reo.semantics.rulebasedautomata;

import java.util.Map;

import nl.cwi.reo.interpret.ports.Port;

public interface DataConstraint {

	/**
	 * Computes the condition on input ports by existentially quantifying over
	 * output ports.
	 * 
	 * @return existential quantification of this constraint over all output
	 *         ports.
	 */
	public DataConstraint getGuard();

	/**
	 * Computes an assignment of data terms to output ports that satisfies this
	 * data constraint.
	 * 
	 * @return Assignment of data terms to output ports.
	 */
	public Map<Port, DataTerm> getAssignment();

}
