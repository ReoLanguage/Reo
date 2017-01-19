package nl.cwi.reo.interpret.systems;

import java.util.Map;

import nl.cwi.reo.errors.CompilationException;
import nl.cwi.reo.interpret.blocks.ReoBlock;
import nl.cwi.reo.interpret.ranges.Expression;
import nl.cwi.reo.interpret.ranges.ExpressionList;
import nl.cwi.reo.interpret.variables.VariableName;
import nl.cwi.reo.interpret.variables.VariableNameList;
import nl.cwi.reo.semantics.Semantics;

public interface ReoSystem<T extends Semantics<T>> extends Expression {

	/**
	 * 
	 * @param values
	 * @param iface
	 * @return 
	 * @throws Exception
	 */
	public ReoBlock<T> instantiate(ExpressionList values, VariableNameList iface) throws CompilationException;

	/**
	 * Substitutes (component) variables with (component) expressions.
	 * @param param			collection of known assignments.
	 * @return Component expression whose body is evaluated using known assignments.
	 * @throws Exception 
	 */
	public ReoSystem<T> evaluate(Map<VariableName, Expression> params) throws CompilationException;
}
