package nl.cwi.reo.interpret.parameters;

import java.util.ArrayList;
import java.util.List;

import nl.cwi.reo.interpret.Expression;
import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of a list of variable expressions of generic type, 
 * such as lists of ports, lists of nodes, and lists of parameters.
 */
public final class ParameterListExpression implements Expression<List<Parameter>> {

	/**
	 * List of variable expressions.
	 */
	private final List<ParameterExpression> list;

	/**
	 * Constructs a new list of variable expressions.
	 * @param list	list of variable expressions
	 */
	public ParameterListExpression(List<ParameterExpression> list) {
		this.list = list;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Parameter> evaluate(Scope s, Monitor m) {
		List<Parameter> concatenation = new ArrayList<Parameter>();
		for (ParameterExpression e : list)
			concatenation.addAll(e.evaluate(s, null));
		return concatenation;
	}

}
