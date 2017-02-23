package nl.cwi.reo.interpret.ports;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.variables.VariableExpression;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of a port expression.
 */
public final class PortExpression extends VariableExpression {
	
	/**
	 * Priority type: ampersand, plus, none.
	 */
	private final PrioType prio;

	/**
	 * Constructs a new port expression.
	 * @param prio	priority type of port
	 * @param var 	variable defining port 
	 */
	public PortExpression(PrioType prio, VariableExpression var) {
		super(var.getName(), var.getIndices(), var.getLocation());
		this.prio = prio;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PortList evaluate(Scope s, Monitor m) {
//		List<Port> ports = new ArrayList<Port>();
//		for (Variable x : super.evaluate(s, m).getIdentifiers())
//			ports.add(new Port(x.getName(), PortType.NONE, prio, new TypeTag(""), true));
//		return new IdentifierList(ports);
//		return new Port(null);
		return null;
	}
}
