package nl.cwi.reo.interpret.components;

import java.util.Set;

import nl.cwi.reo.interpret.Expression;
import nl.cwi.reo.interpret.variables.Identifier;

// TODO: Auto-generated Javadoc
/**
 * Interpretation of a component definition.
 */
public interface ComponentExpression extends Expression<Component> {

	/**
	 * Gets the of variables used in this expression that are not defined
	 * locally. The set need not be complete, because variable indices are
	 * ignored.
	 * 
	 * @return set of undefined variables.
	 */
	public Set<Identifier> getVariables();

}
