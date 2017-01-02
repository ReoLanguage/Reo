package nl.cwi.reo.interpret;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class Signature implements ParameterType {
	
	// TODO allow a signature of the form <x[1..k]>() to be instantiated with an arbitrary
	// number of paramter values, and assign their cardinality to k.
	
	private final ParameterList params;
	
	private final NodeList nodes;
	
	public Signature(ParameterList params, NodeList nodes) {
		this.params = params;
		this.nodes = nodes;
	}
	
	public SignatureInstance evaluate(ExpressionList values, Interface iface) throws Exception {
		
		// Find the assignment of parameters.
		DefinitionList definitions = new DefinitionList();		
		
		Iterator<Parameter> param = params.evaluate(new DefinitionList()).getList().iterator();
		Iterator<Expression> value = values.getList().iterator();	
		
		while (param.hasNext() && value.hasNext()) {
			Parameter x = param.next();
			Expression v = value.next();
			
			if (!(x.getVariable() instanceof VariableName)) 
				throw new Exception("Parameter " + x + " is not a valid parameter name.");
			
			definitions.put((VariableName)x.getVariable(), v);
		}
		
		// Find the links of the interface. 
		Map<Node, Node> links = new HashMap<Node, Node>();	
		
		Iterator<Node> node = nodes.evaluate(definitions).getList().iterator();
		Iterator<Variable> var = iface.getList().iterator();
		
		while (node.hasNext() && var.hasNext()) {
			Node x = node.next();
			Variable v = var.next();
			
			if (!(x.getVariable() instanceof VariableName))
				throw new Exception("Node " + x + " is not a valid node name.");
			
			if (!(v instanceof VariableName))
				throw new Exception("Node " + v + " is not a valid node name.");
			
			links.put(x, x.rename(v));
		}
		
		return new SignatureInstance(definitions, links);
	}

	@Override
	public boolean equalType(ParameterType t) {
		// TODO Auto-generated method stub
		return false;
	}

}
