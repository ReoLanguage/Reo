package nl.cwi.reo.interpret;

import java.util.Map;


public interface ComponentExpression extends Expression {

	/**
	 * Substitutes (component) variables with (component) expressions.
	 */
	public ComponentExpression evaluate(Map<VariableName, Expression> params) 
			throws Exception;

}
