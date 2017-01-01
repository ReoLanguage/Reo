package nl.cwi.reo.interpret;

import java.util.ArrayList;
import java.util.HashMap;import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SignatureExpression implements ParameterType {
	
//	/**
//	 * The unique free variable used for variable length input, 
//	 * e.g., <x[1..k]:int>() allows any number of parameters and
//	 * assigns the actual number of parameters to k.
//	 */
//	private Variable k;

	private List<? extends Parameter> parameters;
	
	private List<? extends Node> intface;
	
	public SignatureExpression(List<? extends Parameter> params, List<? extends Node> intface) {
		this.parameters = params;
		this.intface = intface;
	}

	/**
	 * Computes the parameter assignment and interface relabeling after instantiating this
	 * signature expression with parameters and nodes.
	 * @param values			list of parameter values
	 * @param nodes			list of node variables
	 * @return SignatureValue containing the derived parameter assignment and interface relabeling,
	 * or null if the provided signature does not match this signature expression.
	 */
	public SignatureValue evaluate(ExpressionList values, VariableList nodes) throws Exception {
		
		// Check if the parameters and interface are ready to be substituted.
		boolean canInstantiate = true;
		List<Expression> value_list = new ArrayList<Expression>();
		List<VariableName> node_list = new ArrayList<VariableName>();
		
		for (Expression p : values.getList()) {
			if (p instanceof Sequence) {
				canInstantiate = false;
			} else {
				value_list.add(p);
			}
		}
		
		for (Variable x : nodes.getList()) {
			if (x instanceof VariableName) {
				node_list.add((VariableName)x);
			} else {
				canInstantiate = false;
			}
		}
		
		
		if (canInstantiate) {
			boolean wellDefined = true;
			List<ParameterName> concreteParameters = new ArrayList<ParameterName>();
			for (Parameter pe : parameters) {
				Parameter x = pe.evaluate(new DefinitionList());
				
				if (x instanceof ParameterName) {
					concreteParameters.add((ParameterName)x);
				} else if (x instanceof ParameterList) {
					concreteParameters.addAll(((ParameterList)x).getList());
				} else {
					wellDefined = false;				
				}
			}
			
			if (wellDefined) {

				// Find the assignment of parameters
				DefinitionList defs = new DefinitionList();
				
				Iterator<ParameterName> sign_params = concreteParameters.iterator();
				Iterator<Expression> inst_params = value_list.iterator();
				
				while (sign_params.hasNext() && inst_params.hasNext()) {
					ParameterName p = sign_params.next();
					defs.put(new VariableName(p.getName()), inst_params.next());
				}
				
				if (sign_params.hasNext() || inst_params.hasNext())
					throw new Exception("Parameter do not match.");
				
				// Find the interface renaming 
				Map<NodeName, NodeName> r = new HashMap<NodeName, NodeName>();	
				boolean properInterface = true;
				List<NodeName> concreteNodes = new ArrayList<NodeName>();
				for (Node pe : intface) {
					Node x = pe.evaluate(new DefinitionList());
					
					if (x instanceof NodeName) {
						concreteNodes.add((NodeName)x);
					} else if (x instanceof NodeList) {
						concreteNodes.addAll(((NodeList)x).getList());
					} else {
						properInterface = false;				
					}
				}

				if (properInterface) {
					Iterator<NodeName> nodes_it = concreteNodes.iterator();
					Iterator<VariableName> iface_it = node_list.iterator();
					
					while (nodes_it.hasNext() && iface_it.hasNext()) {
						NodeName n = new NodeName(iface_it.next().getName(), new TypeTag(""), NodeType.FREE, false);
					    r.put(nodes_it.next(), n);
					}
					
					if (nodes_it.hasNext() || iface_it.hasNext())
						throw new Exception("Interfaces do not match.");
					
					return new SignatureValue(defs, r);
				}
			}
			
		}
		
		return null;
	}
	
	@Override
	public boolean equalType(ParameterType t) {
	    if (!(t instanceof SignatureExpression)) return false;
	    SignatureExpression signexpr = (SignatureExpression)t;
	   	return Objects.equals(this.parameters, signexpr.parameters) && 
	   			Objects.equals(this.intface, signexpr.intface);
	}
}
