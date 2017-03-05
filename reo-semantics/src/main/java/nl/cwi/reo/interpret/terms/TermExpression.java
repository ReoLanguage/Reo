package nl.cwi.reo.interpret.terms;

import java.util.List;
import java.util.Set;

import nl.cwi.reo.interpret.Expression;
import nl.cwi.reo.interpret.variables.Identifier;

/**
 * Interpretation of a term.
 */
public interface TermExpression extends Expression<List<Term>> {

	/**
	 * Gets the of variables used in this expression that are not defined
	 * locally. The set need not be complete, because variable indices are
	 * ignored.
	 * 
	 * @return set of undefined variables.
	 */
	public Set<Identifier> getVariables();

}
