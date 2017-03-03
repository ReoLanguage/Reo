package nl.cwi.reo.interpret.variables;

import java.util.ArrayList;
import java.util.List;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.ports.PortType;
import nl.cwi.reo.interpret.ports.PrioType;
import nl.cwi.reo.interpret.typetags.TypeTag;
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
	 * 
	 * @param type
	 *            port type: input/output/none
	 * @param prio
	 *            priority type: ampersand/plus/none
	 * @param tag
	 *            type tag
	 * @param name
	 *            node name
	 * @param indices
	 *            node indices
	 * @param location
	 *            location of node in Reo source file.
	 */
	public NodeExpression(VariableExpression var, PortType type, TypeTag tag) {
		super(var.getName(), var.getIndices(), var.getLocation());
		this.type = type;
		this.prio = PrioType.NONE;
		this.tag = tag;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Nullable
	public List<Port> evaluate(Scope s, Monitor m) {
		List<Port> ports = new ArrayList<Port>();
		List<? extends Identifier> list = super.evaluate(s, m);
		if (list == null)
			return null;
		for (Identifier x : list)
			ports.add(new Port(x.toString(), type, prio, tag, true));
		return ports;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return prio + super.toString() + type + tag;
	}
}
