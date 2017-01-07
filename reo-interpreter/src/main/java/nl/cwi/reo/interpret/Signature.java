package nl.cwi.reo.interpret;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import nl.cwi.reo.semantics.Port;

public final class Signature implements ParameterType {
	
	// TODO allow a signature of the form <x[1..k]>() to be instantiated with an arbitrary
	// number of paramter values, and assign their cardinality to k.
	
	private final ParameterList params;
	
	private final NodeList nodes;
	
	public Signature() {
		this.params = new ParameterList();
		this.nodes = new NodeList();
	}
	
	public Signature(ParameterList params, NodeList nodes) {
		if (params == null || nodes == null)
			throw new IllegalArgumentException("Arguments cannot be null.");
		this.params = params;
		this.nodes = nodes;
	}
	
	public SignatureInstance evaluate(ExpressionList values, Interface iface) throws Exception {
		
		// Find the assignment of parameters.
		Map<VariableName, Expression> definitions = new HashMap<VariableName, Expression>();		
		
		Iterator<Parameter> param = params.evaluate(definitions).getList().iterator();
		Iterator<Expression> value = values.getList().iterator();	
		
		while (param.hasNext() && value.hasNext()) {
			Parameter x = param.next();
			Expression v = value.next();
			
			if (!(x.getVariable() instanceof VariableName)) 
				throw new Exception("Parameter " + x + " is not a valid parameter name.");
			
			definitions.put((VariableName)x.getVariable(), v);
		}
		
		// Find the links of the interface. 
		Map<Port, Port> links = new HashMap<Port, Port>();	
		
		Iterator<Node> node = nodes.evaluate(definitions).getList().iterator();
		Iterator<Variable> var = iface.getList().iterator();
		
		while (node.hasNext() && var.hasNext()) {
			Node x = node.next();
			Variable v = var.next();
			
			Port src = x.toPort();
			Port snk = x.rename(v).toPort();
			
			if (src == null)
				throw new Exception("Port " + x + " is not a valid node name.");
			
			if (snk == null)
				throw new Exception("Port " + v + " is not a valid node name.");
			
			links.put(src, snk);
		}
		
		return new SignatureInstance(definitions, links);
	}

	@Override
	public boolean equalType(ParameterType t) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public String toString() {
		return "" + params + nodes;
	}

}
