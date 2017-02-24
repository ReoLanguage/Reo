package nl.cwi.reo.interpret.ports;

import java.util.ArrayList;
import java.util.List;

import nl.cwi.reo.interpret.Expression;
import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of a list of port expressions.
 */
public final class PortListExpression implements Expression<List<Port>> {

	/**
	 * List of variable expressions.
	 */
	private final List<PortExpression> list;

	/**
	 * Constructs a new list of variable expressions.
	 * @param list	list of variable expressions
	 */
	public PortListExpression(List<PortExpression> list) {
		this.list = list;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Port> evaluate(Scope s, Monitor m) {
		List<Port> concatenation = new ArrayList<Port>();
		for (PortExpression e : list)
			concatenation.addAll(e.evaluate(s, null));
		return concatenation;
	}

}
