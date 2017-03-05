package nl.cwi.reo.interpret.instances;

import java.util.Set;

import nl.cwi.reo.interpret.Expression;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.semantics.Semantics;

/**
 * Interpretation of a (set of) instances.
 * 
 * @param <T>
 *            Reo semantics type
 */
public interface InstanceExpression<T extends Semantics<T>> extends Expression<Instance<T>> {

	/**
	 * Gets the of variables used in this expression that are not defined
	 * locally. The set need not be complete, because variable indices are
	 * ignored.
	 * 
	 * @return set of undefined variables.
	 */
	public Set<Identifier> getVariables();

}
