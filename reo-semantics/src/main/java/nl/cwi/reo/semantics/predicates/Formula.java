package nl.cwi.reo.semantics.predicates;

import java.util.Map;
import java.util.Set;

import nl.cwi.reo.interpret.Expression;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.typetags.TypeTag;

/**
 * A first-order formula over the language of constraints.
 * 
 * <p>
 * Every implementing class is immutable.
 */
public interface Formula extends Expression<Formula> {

	/**
	 * Gets the set of all free variables in this formula.
	 * 
	 * @return set of all free variables.
	 */
	public Set<Variable> getFreeVariables();

	/**
	 * Returns the set of ports that occur as a variables in this formula
	 * 
	 * @return set of port variables in this formula.
	 */
	public Set<Port> getPorts();

	/**
	 * Checks if this formula contains a quantifier.
	 * 
	 * @return true if this formula contains a quantifier.
	 */
	public boolean isQuantifierFree();

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
	 * Infer type of a term from a set of set of term.
	 * 
	 * Each set of term represents terms with the same type.
	 * @param termTypeList
	 * @return
	 */
	public Set<Set<Term>> inferTermType(Set<Set<Term>> termTypeList);

	/**
	 * Type the term of the formula according to the termTypedList.
	 * 
	 * @param termTypeList
	 * @return
	 */
	public Formula getTypedFormula(Map<Term,TypeTag> typeMap);
	
	/**
	 * Substitutes a term t for every occurrence of a variable x in this
	 * formula.
	 * 
	 * @param t
	 *            substituted term
	 * @param x
	 *            free variable
	 * @return substituted formula.
	 */
	public Formula substitute(Term t, Variable x);

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
