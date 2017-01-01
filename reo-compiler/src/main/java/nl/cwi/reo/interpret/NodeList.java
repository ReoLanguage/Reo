package nl.cwi.reo.interpret;

import java.util.List;

public class NodeList implements Node, Sequence {
	
	private List<NodeName> nodes;
	
	public NodeList(List<NodeName> nodes) {
		this.nodes = nodes;
	}
	
	public List<NodeName> getList() {
		return nodes;
	}

	@Override
	public Node evaluate(DefinitionList params)
			throws Exception {
		return this;
	}


}
