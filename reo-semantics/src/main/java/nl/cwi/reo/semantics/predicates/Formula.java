package nl.cwi.reo.semantics.predicates;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import nl.cwi.reo.interpret.Expression;
import nl.cwi.reo.interpret.ports.Port;

/**
 * The Interface Formula.
 */
public interface Formula extends Expression<Formula> {

	/**
	 * Returns the set of node variables in the formula, and casts them to
	 * ports.
	 * 
	 * @return set of port variables.
	 */
	public Set<Port> getInterface();

	/**
	 * Renames the free port variables in the formula.
	 * 
	 * @param links
	 *            assignment from old to new port variables.
	 * @return Formula with renamed port variables.
	 */
	public Formula rename(Map<Port, Port> links);

	/**
	 * Transforms the formula in negation normal form.
	 * 
	 * @return equivalent formula in negation normal form.
	 */
	public Formula NNF();

	/**
	 * Transforms the formula into disjunctive normal form.
	 * 
	 * @return equivalent formula in disjunctive normal form.
	 */
	public Formula DNF();

	/**
	 * Substitutes a term t for every occurrence of a variable x in this
	 * formula.
	 * 
	 * @param t
	 *            substituted term
	 * @param x
	 *            free variable
	 * @return substituted term.
	 */
	public default Formula substitute(Term t, Variable x) {
		Map<Variable, Term> map = new HashMap<>();
		map.put(x, t);
		return substitute(map);
	}

	/**
	 * Substitutes a term t for every occurrence of a variable x in this
	 * formula.
	 * 
	 * @param map
	 *            assignment of terms to variables
	 * @return substituted term.
	 */
	public Formula substitute(Map<Variable, Term> map);

	/**
	 * Gets the set of all free variables in this formula.
	 * 
	 * @return set of all free variables.
	 */
	public Set<Variable> getFreeVariables();

	/**
	 * Tries to determine which variables in this formula must evaluate to null
	 * and which variables must evaluate to a non-null datum.
	 * 
	 * @return map that assigns 0 to a variable, if that variable must evaluate
	 *         to null, or 1, if that variable must evaluate to a non-null
	 *         datum, or nothing, otherwise.
	 */
	public Map<Variable, Integer> getEvaluation();

}
