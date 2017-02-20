package nl.cwi.reo.interpret.predicates;

import nl.cwi.reo.interpret.Expression;
import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of a predicate expression.
 */
public interface PredicateExpression extends Expression<Predicate> {
	
	/**
	 * Computes a list of all possible extensions of a given set of parameter 
	 * assignments that satisfy this formula.
	 * @param s		variable assignment
	 * @param m		message container
	 * @return list of all possible extensions.
	 */
	public Predicate evaluate(Scope s, Monitor m);

}
