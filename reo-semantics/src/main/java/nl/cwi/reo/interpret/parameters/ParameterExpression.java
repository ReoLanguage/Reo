package nl.cwi.reo.interpret.parameters;

import java.util.ArrayList;
import java.util.List;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.terms.Term;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.interpret.variables.IdentifierList;
import nl.cwi.reo.interpret.variables.VariableExpression;
import nl.cwi.reo.util.Location;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of a parameter expression.
 */
public final class ParameterExpression extends VariableExpression<Parameter> {

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
	public ParameterExpression(String name, List<List<Term>> indices, Location location, ParameterType type) {
		super(name, indices, location);
		this.type = type;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IdentifierList<Parameter> evaluate(Scope s, Monitor m) {
		List<Parameter> params = new ArrayList<Parameter>();
		for (Identifier x : super.evaluate(s, m).getIdentifiers())
			params.add(new Parameter(x.getName(), type));
		return new IdentifierList<Parameter>(params);
	}
}
