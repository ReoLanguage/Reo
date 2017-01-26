package nl.cwi.reo.interpret.expressions;

import java.util.Map;

import nl.cwi.reo.semantics.api.Expression;

/**
 * An ordered list of expressions.
 */
public interface Expressions extends Expression {
	
	public Expressions evaluate(Map<String, Expression> params);

}
