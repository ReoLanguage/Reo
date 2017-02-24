package nl.cwi.reo.interpret.nodes;

import java.util.ArrayList;
import java.util.List;

import nl.cwi.reo.interpret.Expression;
import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of a list of node expressions.
 */
public final class NodeListExpression implements Expression<List<Port>> {

	/**
	 * List of variable expressions.
	 */
	private final List<NodeExpression> list;

	/**
	 * Constructs a new list of variable expressions.
	 * @param list	list of variable expressions
	 */
	public NodeListExpression(List<NodeExpression> list) {
		this.list = list;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Port> evaluate(Scope s, Monitor m) {
		List<Port> concatenation = new ArrayList<Port>();
		for (NodeExpression e : list)
			concatenation.addAll(e.evaluate(s, null));
		return concatenation;
	}

}
