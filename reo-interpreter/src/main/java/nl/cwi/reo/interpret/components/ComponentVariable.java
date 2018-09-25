package nl.cwi.reo.interpret.components;

import java.util.List;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.values.Value;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.interpret.variables.VariableExpression;
import nl.cwi.reo.util.Monitor;

// TODO: Auto-generated Javadoc
/**
 * Interpretation of a component variable.
 */
public final class ComponentVariable implements ComponentExpression {

	/**
	 * Variable.
	 */
	private final VariableExpression variable;

	/**
	 * Constructs a new component variable.
	 * 
	 * @param var
	 *            component variable
	 */
	public ComponentVariable(VariableExpression var) {
		this.variable = var;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Nullable
	public Component evaluate(Scope s, Monitor m) {
		List<? extends Identifier> ids = variable.evaluate(s, m);
		if (ids == null || ids.isEmpty())
			return null;
		Value v = s.get(ids.get(0));
		return (v instanceof Component ? (Component) v : null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Identifier> getVariables() {
		return variable.getVariables();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "" + variable;
	}
}
