package nl.cwi.reo.semantics.api;

import java.util.Map;

/**
 * A class implementing this interface can be evaluated in a set of parameters.
 * Each parameter value is given as a key-value pair containing the name of the 
 * parameter and the its value, respectively. Parameter values can itself be an 
 * expression.
 * 
 * @param <T> type of returned object after evaluation
 * @see Expression
 */
public interface Evaluable<T> {
	
	/**
	 * Substitutes expressions for free variables in this expression.
	 * @param params			parameter assignment
	 * @return new expression with variables substituted.
	 * @throws CompilationException if any substituted expression is of the wrong type.
	 */
	public T evaluate(Map<String, Expression> params);
}
