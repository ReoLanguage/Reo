package nl.cwi.reo.interpret.components;

import java.util.Map;

import nl.cwi.reo.interpret.programs.ProgramExpression;
import nl.cwi.reo.interpret.ranges.Expression;
import nl.cwi.reo.interpret.ranges.ExpressionList;
import nl.cwi.reo.interpret.variables.VariableName;
import nl.cwi.reo.interpret.variables.VariableNameList;
import nl.cwi.reo.semantics.Semantics;


public interface ComponentExpression<T extends Semantics<T>> extends Expression {

	/**
	 * 
	 * @param values
	 * @param iface
	 * @return 
	 * @throws Exception
	 */
	public ProgramExpression<T> instantiate(ExpressionList values, VariableNameList iface) 
			throws Exception;

	/**
	 * Substitutes (component) variables with (component) expressions.
	 * @param param			collection of known assignments.
	 * @return Component expression whose body is evaluated using known assignments.
	 * @throws Exception 
	 */
	public ComponentExpression<T> evaluate(Map<VariableName, Expression> params) 
			throws Exception;
}
