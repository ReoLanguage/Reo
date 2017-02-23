package nl.cwi.reo.interpret.parameters;

import java.util.ArrayList;
import java.util.List;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.terms.Term;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.interpret.variables.VariableExpression;
import nl.cwi.reo.util.Location;
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
	public Parameter evaluate(Scope s, Monitor m) {
		return new Parameter(this.name,type);
	}
}
