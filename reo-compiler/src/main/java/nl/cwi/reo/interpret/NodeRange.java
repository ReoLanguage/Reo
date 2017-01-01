package nl.cwi.reo.interpret;

import java.util.ArrayList;
import java.util.List;

public class NodeRange implements Node, Sequence {
	
	private VariableRange var;

	private TypeTag type;
	
	private NodeType pol;

	public NodeRange(VariableRange var, TypeTag type, NodeType pol) {
		this.var = var;
		this.type = type;
		this.pol = pol;
	}
	
	@Override
	public Node evaluate(DefinitionList params) throws Exception {		
		Expression x = var.evaluate(params);		
		if (x instanceof VariableName) {
			return new NodeName(((VariableName)x).getName(), type, pol, false);	
		} else if (x instanceof ExpressionList) {
			List<NodeName> nodes = new ArrayList<NodeName>();
			for (Expression v : ((ExpressionList)x).getList())
				if (v instanceof VariableName)
					nodes.add(new NodeName(((VariableName)v).getName(), type, pol, false));
			return new NodeList(nodes);
		}		
		return this;
	}

}
