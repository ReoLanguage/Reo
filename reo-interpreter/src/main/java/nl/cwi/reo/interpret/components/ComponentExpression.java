package nl.cwi.reo.interpret.components;

import java.util.Map;

import nl.cwi.reo.interpret.arrays.Expression;
import nl.cwi.reo.interpret.arrays.ExpressionRange;
import nl.cwi.reo.interpret.programs.ProgramExpression;
import nl.cwi.reo.interpret.signatures.Interface;
import nl.cwi.reo.interpret.variables.VariableName;


public interface ComponentExpression extends Expression {

	/**
	 * 
	 * @param values
	 * @param iface
	 * @return 
	 * @throws Exception
	 */
	public ProgramExpression instantiate(ExpressionRange values, Interface iface) 
			throws Exception;

	/**
	 * Substitutes (component) variables with (component) expressions.
	 * @param param			collection of known assignments.
	 * @return Component expression whose body is evaluated using known assignments.
	 * @throws Exception 
	 */
	public ComponentExpression evaluate(Map<VariableName, Expression> params) 
			throws Exception;
}
