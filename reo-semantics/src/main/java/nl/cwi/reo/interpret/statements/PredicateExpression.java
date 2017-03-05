package nl.cwi.reo.interpret.statements;

import java.util.List;
import java.util.Set;

import nl.cwi.reo.interpret.Expression;
import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of a predicate expression.
 */
public interface PredicateExpression extends Expression<List<Scope>> {

	/**
	 * Computes a list of all possible extensions of a given set of parameter
	 * assignments that satisfy this formula.
	 * 
	 * @param s
	 *            variable assignment
	 * @param m
	 *            message container
	 * @return list of all possible extensions.
	 */
	public List<Scope> evaluate(Scope s, Monitor m);

	/**
	 * Gets the of variables used in this expression that are not defined
	 * locally. The set need not be complete, because variable indices are
	 * ignored.
	 * 
	 * @return set of undefined variables.
	 */
	public Set<Identifier> getVariables();

}
