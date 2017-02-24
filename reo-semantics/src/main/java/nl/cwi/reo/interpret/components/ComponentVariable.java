package nl.cwi.reo.interpret.components;

import java.util.List;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.connectors.Semantics;
import nl.cwi.reo.interpret.values.Value;
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
	private final VariableExpression var;

	/**
	 * Constructs a new component variable.
	 * @param var	component variable
	 */
	public ComponentVariable(VariableExpression var) {
		this.var = var;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Component<T> evaluate(Scope s, Monitor m) {
		List<? extends Identifier> ids = var.evaluate(s, m);
		if (ids == null || ids.isEmpty()) return null;
		Value v = s.get(ids.get(0));
		return (v instanceof Component<?> ? (Component<T>)v : null);
	}
}
