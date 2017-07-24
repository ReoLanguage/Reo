package nl.cwi.reo.interpret.statements;

import java.util.List;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Expression;
import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.util.Monitor;

// TODO: Auto-generated Javadoc
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
	@Nullable
	public List<Scope> evaluate(Scope s, Monitor m);

	/**
	 * Gets the of variables used in this expression that are not defined
	 * locally. The set need not be complete, because variable indices are
	 * ignored.
	 * 
	 * @return set of undefined variables.
	 */
	public Set<Identifier> getVariables();

	/**
	 * Returns the set of all variables that have no indices and are defined by
	 * the predicate, given an initial set of defined variables.
	 *
	 * @param defns
	 *            the defns
	 * @return set of defined variables, or null if not all variables are
	 *         defined.
	 */
	@Nullable
	public Set<Identifier> getDefinedVariables(Set<Identifier> defns);

}
