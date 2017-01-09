package nl.cwi.reo.interpret;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nl.cwi.reo.semantics.Port;
import nl.cwi.reo.semantics.PortType;

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
				p = new Port(vname.getName(), PortType.IN, tag.name(), false);
				break;
			case SINK:
				p = new Port(vname.getName(), PortType.OUT, tag.name(), false);
				break;
			default: 
				p = new Port(vname.getName(), PortType.UNKNOWN, tag.name(), false);
				break;
			}
		}
		return p;
	}
	
	public List<Node> getList() {
		List<Node> nodes = null;
		if (var instanceof VariableNameList) {
			nodes = new ArrayList<Node>();
			for (VariableName v : ((VariableNameList)var).getList())
				nodes.add(new Node(v, type, tag));
		}
		return nodes;
	}
	
	public Node rename(Variable var) {
		return new Node(var, type, tag);
	}

	@Override
	public Node evaluate(Map<VariableName, Expression> params) throws Exception {
		return new Node(var.evaluate(params), type, tag);
	}
	
	@Override
	public String toString() {
		return "" + var + type + tag;
	}
}
