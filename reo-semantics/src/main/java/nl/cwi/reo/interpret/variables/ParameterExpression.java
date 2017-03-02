package nl.cwi.reo.interpret.variables;

import java.util.ArrayList;
import java.util.List;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.util.Monitor;

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
	 * @param name		parameter name
	 * @param indices	parameter indices
	 * @param location	location of parameter in Reo source file
	 * @param type		type of parameter
	 */
	public ParameterExpression(VariableExpression var, ParameterType type) {
		super(var.getName(),var.getIndices(),var.getLocation());
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
		if (list == null) return null;
		for (Identifier x : list)
			params.add(new Parameter(x.toString(), type));
		return params;
	}
}
