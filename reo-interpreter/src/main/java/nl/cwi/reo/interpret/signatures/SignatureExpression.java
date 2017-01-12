package nl.cwi.reo.interpret.signatures;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import nl.cwi.reo.interpret.arrays.Expression;
import nl.cwi.reo.interpret.arrays.ExpressionRange;
import nl.cwi.reo.interpret.semantics.Definitions;
import nl.cwi.reo.interpret.variables.Variable;
import nl.cwi.reo.interpret.variables.VariableName;
import nl.cwi.reo.interpret.variables.VariableRange;
import nl.cwi.reo.semantics.Port;

public final class SignatureExpression implements ParameterType {
	
	// TODO allow a signature of the form <x[1..k]>() to be instantiated with an arbitrary
	// number of paramter values, and assign their cardinality to k.
	
	private final ParameterList params;
	
	private final NodeList nodes;
	
	public SignatureExpression() {
		this.params = new ParameterList();
		this.nodes = new NodeList();
	}
	
	public SignatureExpression(ParameterList params, NodeList nodes) {
		if (params == null || nodes == null)
			throw new NullPointerException();
		this.params = params;
		this.nodes = nodes;
	}
	
	public Signature evaluate(ExpressionRange values, Interface iface) throws Exception {
		
		Definitions definitions = new Definitions();		

		// Try to find the parameter value for a correct number of nodes
		int k_params = 0;
		VariableRange rng_params = null;
		for (Parameter param : params) {
			if (param.getVariable() instanceof VariableName) {
				k_params += 1;
			} else if (param.getVariable() instanceof VariableRange) {
				rng_params = (VariableRange)param.getVariable();
			} else {
				throw new Exception("Parameter " + param.getVariable() + " is not a valid parameter name.");
			}
		}
		int size_params = values.size() - k_params;
		
		if (rng_params != null) {
			Definitions defs = rng_params.findParamFromSize(size_params);
			if (defs != null) {
				definitions.putAll(defs);
			} else {
				System.out.println("[error] Parameters in " + rng_params + " cannot be deduced from its length.");				
			}
		} else {
			if (size_params != 0)
				System.out.println("[error] Wrong number of parameter values: " + values);
		}

		// Try to find the parameter value for a correct number of nodes
		int k_nodes = 0;
		VariableRange rng_nodes = null;
		for (Node node : nodes) {
			if (node.getVariable() instanceof VariableName) {
				k_nodes += 1;
			} else if (node.getVariable() instanceof VariableRange) {
				rng_nodes = (VariableRange)node.getVariable();
			} else {
				throw new Exception("Parameter " + node.getVariable() + " is not a valid parameter name.");
			}
		}
		int size_nodes = iface.size() - k_nodes;
		
		if (rng_nodes != null) {
			Definitions defs = rng_nodes.findParamFromSize(size_nodes);
			if (defs != null) {
				definitions.putAll(defs);
			} else {
				System.out.println("[error] Parameters in " + rng_nodes + " cannot be deduced from its length.");				
			}
		} else {
			if (size_nodes != 0)
				System.out.println("[error] Wrong number of parameter values: " + iface);
		}

		// Evaluate the parameters and nodes using the found parameters.
		ParameterList _params = params.evaluate(definitions);
		NodeList _nodes = nodes.evaluate(definitions);

		// Find the assignment of parameters.
		Iterator<Parameter> param = _params.iterator();
		Iterator<Expression> value = values.iterator();	
		
		while (param.hasNext() && value.hasNext()) {
			Parameter x = param.next();
			Expression v = value.next();
			
			if (!(x.getVariable() instanceof VariableName)) 
				throw new Exception("Parameter " + x + " is not a valid parameter name.");
			
			definitions.put((VariableName)x.getVariable(), v);
		}
		
		// Find the links of the interface. 
		Map<Port, Port> links = new HashMap<Port, Port>();	
		
		Iterator<Node> node = _nodes.evaluate(definitions).iterator();
		Iterator<Variable> var = iface.iterator();
		
		while (node.hasNext() && var.hasNext()) {
			Node x = node.next();
			Variable v = var.next();
			
			Port src = x.toPort();
			Port snk = x.rename(v).toPort();
			
			if (src == null)
				throw new Exception(x + " is not a valid node name.");
			
			if (snk == null)
				throw new Exception(v + " is not a valid node name.");
			
			links.put(src, snk);
		}
		
		return new Signature(definitions, links);
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
