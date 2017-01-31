package nl.cwi.reo.interpret.systems;

import java.util.Map;

import nl.cwi.reo.interpret.blocks.ReoBlock;
import nl.cwi.reo.interpret.expressions.ValueExpression;
import nl.cwi.reo.interpret.expressions.ValueList;
import nl.cwi.reo.interpret.variables.VariableNameList;
import nl.cwi.reo.semantics.api.Expression;
import nl.cwi.reo.semantics.api.Semantics;

public interface ReoSystem<T extends Semantics<T>> extends ValueExpression {

	/**
	 * Instantiates the parameters and nodes in the body of a component definition.
	 * @param values	parameter values
	 * @param iface		nodes in the interface
	 * @return The instantiated body of this definition, or null
	 */
	public ReoBlock<T> instantiate(ValueList values, VariableNameList iface);

	/**
	 * Substitutes (component) variables with (component) expressions.
	 * @param param			collection of known assignments.
	 * @return Component expression whose body is evaluated using known assignments.
	 * @throws Exception 
	 */
	public ReoSystem<T> evaluate(Map<String, Expression> params);
}
