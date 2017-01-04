package nl.cwi.reo.interpret;

import java.util.Map;


public interface Component extends Expression {

	/**
	 * Substitutes (component) variables with (component) expressions.
	 */
	public Component evaluate(Map<VariableName, Expression> params) 
			throws Exception;
	
	/**
	 * 
	 * @param values
	 * @param iface
	 * @return
	 */
	public Component instantiate(ExpressionList values, 
			Interface iface) throws Exception;

}
