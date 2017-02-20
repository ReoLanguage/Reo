package nl.cwi.reo.interpret.nodes;

import java.util.ArrayList;
import java.util.List;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.ports.PortType;
import nl.cwi.reo.interpret.ports.PrioType;
import nl.cwi.reo.interpret.terms.Term;
import nl.cwi.reo.interpret.typetags.TypeTag;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.interpret.variables.IdentifierList;
import nl.cwi.reo.interpret.variables.VariableExpression;
import nl.cwi.reo.util.Location;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of a node expression.
 */
public final class NodeExpression extends VariableExpression<Port> {
	
	/**
	 * Port type: input, output, none.
	 */
	private final PortType type;
	
	/**
	 * Priority type: ampersand, plus, none.
	 */
	private final PrioType prio;
	
	/**
	 * Type tag.
	 */
	private final TypeTag tag;

	/**
	 * Constructs a new node expression.
	 * @param type		port type: input/output/none
	 * @param prio		priority type: ampersand/plus/none
	 * @param tag		type tag
	 * @param name		node name
	 * @param indices	node indices
	 * @param location	location of node in Reo source file. 
	 */
	public NodeExpression(PortType type, PrioType prio, TypeTag tag, String name, List<List<Term>> indices, Location location) {
		super(name, indices, location);
		this.type = type;
		this.prio = prio;
		this.tag = tag;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IdentifierList<Port> evaluate(Scope s, Monitor m) {
		List<Port> ports = new ArrayList<Port>();
		for (Identifier x : super.evaluate(s, m).getIdentifiers())
			ports.add(new Port(x.getName(), type, prio, tag, true));
		return new IdentifierList<Port>(ports);
	}

}
