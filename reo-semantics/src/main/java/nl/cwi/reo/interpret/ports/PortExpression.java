package nl.cwi.reo.interpret.ports;

import java.util.ArrayList;
import java.util.List;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.terms.Term;
import nl.cwi.reo.interpret.typetags.TypeTag;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.interpret.variables.IdentifierList;
import nl.cwi.reo.interpret.variables.VariableExpression;
import nl.cwi.reo.util.Location;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of a port expression.
 */
public final class PortExpression extends VariableExpression<Port> {
	
	/**
	 * Priority type: ampersand, plus, none.
	 */
	private final PrioType prio;

	
	/**
	 * 
	 * Constructs a new port expression.
	 * @param prio	priority type of port
	 * @param var 	variable defining port 
	 */
	public PortExpression(PrioType prio, VariableExpression<Port> var) {
		super(var.getName(), var.getIndices(), var.getLocation());
		this.prio = prio;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IdentifierList<Port> evaluate(Scope s, Monitor m) {
		List<Port> ports = new ArrayList<Port>();
		for (Identifier x : super.evaluate(s, m).getIdentifiers())
			ports.add(new Port(x.getName(), PortType.NONE, prio, new TypeTag(""), true));
		return new IdentifierList<Port>(ports);
	}
}
