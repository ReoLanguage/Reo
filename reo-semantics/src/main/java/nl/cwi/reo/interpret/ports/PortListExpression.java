package nl.cwi.reo.interpret.ports;

import java.util.ArrayList;
import java.util.List;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.stringtemplate.v4.ST;

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
	 * 
	 * @param list
	 *            list of variable expressions
	 */
	public PortListExpression(List<PortExpression> list) {
		this.list = list;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Nullable
	public List<Port> evaluate(Scope s, Monitor m) {
		List<Port> concatenation = new ArrayList<Port>();
		List<Port> listPort;
		for (PortExpression e : list)
			if ((listPort = e.evaluate(s, m)) != null)
				concatenation.addAll(listPort);
			else
				return null;
		return concatenation;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		ST st = new ST("({list; separator=\", \"})", '{', '}');
		st.add("list", list);
		return st.render();
	}

}
