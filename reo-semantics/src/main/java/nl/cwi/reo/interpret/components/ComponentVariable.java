package nl.cwi.reo.interpret.components;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.connectors.Semantics;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.interpret.variables.VariableExpression;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of a component variable.
 * @param <T> Reo semantics type
 */
public final class ComponentVariable<T extends Semantics<T>> implements ComponentExpression<T> {
	
	/**
	 * Variable.
	 */
	private final VariableExpression<Identifier> var;

	/**
	 * Constructs a new component variable.
	 * @param var	component variable
	 */
	public ComponentVariable(VariableExpression<Identifier> var) {
		this.var = var;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Component<T> evaluate(Scope s, Monitor m) {
		// TODO Auto-generated method stub
		return null;
	}

}
