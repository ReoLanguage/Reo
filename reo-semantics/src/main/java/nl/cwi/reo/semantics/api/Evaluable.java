package nl.cwi.reo.semantics.api;

import java.util.Map;

/**
 * A class implementing Evaluable of generi
 * @param <T>
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
