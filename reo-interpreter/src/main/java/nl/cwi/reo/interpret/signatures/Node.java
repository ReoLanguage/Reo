package nl.cwi.reo.interpret.signatures;

import java.util.Map;

import nl.cwi.reo.errors.CompilationException;
import nl.cwi.reo.interpret.variables.Variable;
import nl.cwi.reo.interpret.variables.VariableName;
import nl.cwi.reo.semantics.api.Evaluable;
import nl.cwi.reo.semantics.api.Expression;
import nl.cwi.reo.semantics.api.Port;
import nl.cwi.reo.semantics.api.PortType;
import nl.cwi.reo.semantics.api.PrioType;

/**
 * An immutable node implementation.
 */
public final class Node implements Evaluable<Node> {

	private final Variable var;
	
	private final NodeType type;

	private final TypeTag tag;
	
	public Node(Variable var, NodeType type, TypeTag tag) {
		if (var == null || type == null || tag == null)
			throw new NullPointerException();
		this.var = var;
		this.type = type;
		this.tag = tag;
	}
	
	public Variable getVariable() {
		return var;
	}
	
	public TypeTag getTypeTag() {
		return tag;
	}
	
	public NodeType getNodeType() {
		return type;
	}
	
	public Port toPort() {
		Port p = null;
		if (var instanceof VariableName) { 
			VariableName vname = (VariableName)var;
			switch (type) {
			case SOURCE:
				p = new Port(vname.getName(), PortType.IN, PrioType.NONE, tag.name(), false);
				break;
			case SINK:
				p = new Port(vname.getName(), PortType.OUT, PrioType.NONE, tag.name(), false);
				break;
			default: 
				p = new Port(vname.getName(), PortType.NONE, PrioType.NONE, tag.name(), false);
				break;
			}
		}
		return p;
	}
	
	public Node rename(Variable var) {
		return new Node(var, type, tag);
	}

	@Override
	public Node evaluate(Map<String, Expression> params) throws CompilationException {
		Expression e = var.evaluate(params);
		if (!(e instanceof Variable))
			throw new CompilationException(var.getToken(), "Node variable " + var + " cannot be assigned to " + e);
		return new Node((Variable)e, type, tag);
	}
	
	@Override
	public String toString() {
		return "" + var + type + tag;
	}
}
