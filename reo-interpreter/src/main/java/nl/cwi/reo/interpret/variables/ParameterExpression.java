package nl.cwi.reo.interpret.variables;

import java.util.ArrayList;
import java.util.List;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.typetags.TypeTag;
import nl.cwi.reo.util.Monitor;

// TODO: Auto-generated Javadoc
/**
 * Interpretation of a parameter expression.
 */
public final class ParameterExpression extends VariableExpression {

	/**
	 * Type of this parameter.
	 */
	private final ParameterType type;

	/**
	 * Constructs a new parameter expression.
	 *
	 * @param var
	 *            the var
	 * @param type
	 *            type of parameter
	 */
	public ParameterExpression(VariableExpression var, ParameterType type) {
		super(var.getName(), var.getIndices(), var.getLocation());
		this.type = type;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Nullable
	public List<Parameter> evaluate(Scope s, Monitor m) {
		List<Parameter> params = new ArrayList<Parameter>();
		List<? extends Identifier> list = super.evaluate(s, m);
		if (list == null)
			return null;
		for (Identifier x : list)
			params.add(new Parameter(x.toString(), type));
		return params;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return type instanceof TypeTag ? super.toString() + ":" + type : super.toString();
	}
}
