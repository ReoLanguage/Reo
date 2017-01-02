package nl.cwi.reo.interpret;

import java.util.ArrayList;
import java.util.List;

public final class Node implements Evaluable<Node> {

	private final Variable var;
	
	private final IOType type;

	private final TypeTag tag;
	
	public Node(Variable var, IOType type, TypeTag tag) {
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
	
	public IOType getNodeType() {
		return type;
	}
	
	public Port toPort() {
		Port p = null;
		if (var instanceof VariableName && (type == IOType.SOURCE || type == IOType.SINK))
			p = new Port(((VariableName)var).getName(), type, tag, false);
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
	public Node evaluate(DefinitionList params) throws Exception {
		return new Node(var.evaluate(params), type, tag);
	}	
}
