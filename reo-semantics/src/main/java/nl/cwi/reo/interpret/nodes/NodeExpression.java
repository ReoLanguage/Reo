package nl.cwi.reo.interpret.nodes;

import java.util.ArrayList;
import java.util.List;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.ports.PortType;
import nl.cwi.reo.interpret.ports.PrioType;
import nl.cwi.reo.interpret.terms.Term;
import nl.cwi.reo.interpret.terms.TermsExpression;
import nl.cwi.reo.interpret.typetags.TypeTag;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.interpret.variables.Variable;
import nl.cwi.reo.interpret.variables.VariableExpression;
import nl.cwi.reo.interpret.variables.VariableList;
import nl.cwi.reo.util.Location;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of a node expression.
 */
public final class NodeExpression extends VariableExpression {
	
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
	public NodeExpression(PortType type, PrioType prio, TypeTag tag, String name, List<TermsExpression> indices, Location location) {
		super(name, indices, location);
		this.type = type;
		this.prio = prio;
		this.tag = tag;
	}
	public NodeExpression(VariableExpression var, PortType type, TypeTag tag) {
		super(var.getName(), var.getIndices(), var.getLocation());
		this.type = type;
		this.prio = null;
		this.tag = tag;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Variable evaluate(Scope s, Monitor m) {
		List<Variable> l = new ArrayList<Variable>();
//		VariableList v = new VariableList(l);
		List<Variable> ports = new ArrayList<Variable>();
//		for (Identifier x : super.evaluate(s, m).getIdentifiers())
//			ports.add(new Port(x.getName(), type, prio, tag, true));
		return new Port(null);//IdentifierList<Port>(ports);
	}

}
