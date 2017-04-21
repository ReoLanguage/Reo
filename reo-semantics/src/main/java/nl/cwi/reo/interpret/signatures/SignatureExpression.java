package nl.cwi.reo.interpret.signatures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.stringtemplate.v4.ST;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.terms.Range;
import nl.cwi.reo.interpret.terms.TermExpression;
import nl.cwi.reo.interpret.terms.VariableTermExpression;
import nl.cwi.reo.interpret.typetags.TypeTag;
import nl.cwi.reo.interpret.values.DecimalValue;
import nl.cwi.reo.interpret.values.IntegerValue;
import nl.cwi.reo.interpret.values.StringValue;
import nl.cwi.reo.interpret.values.Value;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.interpret.variables.NodeExpression;
import nl.cwi.reo.interpret.variables.Parameter;
import nl.cwi.reo.interpret.variables.ParameterExpression;
import nl.cwi.reo.interpret.variables.ParameterType;
import nl.cwi.reo.util.Location;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of a component signature.
 */
public final class SignatureExpression implements ParameterType {

	/**
	 * List of parameters.
	 */
	private final List<ParameterExpression> params;

	/**
	 * List of nodes.
	 */
	private final List<NodeExpression> nodes;

	/**
	 * Location of this signature in Reo source file.
	 */
	private final Location location;

	/**
	 * Constructs a new signature expression.
	 * 
	 * @param params
	 *            list of parameters
	 * @param nodes
	 *            list of nodes
	 * @param location
	 *            location of signature in Reo source file.
	 */
	public SignatureExpression(List<ParameterExpression> params, List<NodeExpression> nodes, Location location) {
		this.params = params;
		this.nodes = nodes;
		this.location = location;
	}

	public Set<Identifier> getParams(){
		Set<Identifier> l = new HashSet<Identifier>();
		for(ParameterExpression exp : params){
			l.addAll(exp.getVariables());
		}
		return l;
	}
	
	
	/**
	 * Evaluates this interface for a given list of parameter values and a given
	 * list of ports.
	 * 
	 * @param values
	 *            parameter values
	 * @param ports
	 *            ports in interface
	 * @param m
	 *            message container
	 * @return signature that contains interface renaming and parameter
	 *         assignments.
	 */
	@Nullable
	public Signature evaluate(List<?> values, @Nullable List<Port> ports, Monitor m) {
		Scope s = new Scope();

		// Try to find the parameter value for a correct number of parameters
		int k_params = 0;
		ParameterExpression rng_params = null;
		for (ParameterExpression param : params) {
			if (param.getIndices().isEmpty())
				k_params++;
			for (TermExpression t : param.getIndices()) {
				if (t instanceof Range) {
					rng_params = param;
				} else if (t instanceof Value) {
					k_params += 1;
				} else {
					m.add(location, "Parameter " + param + " cannot have undefined indices.");
					return null;
				}
			}
		}
		int size_params = values.size() - k_params;

		if (rng_params != null) {
			Scope defs = rng_params.findParamFromSize(size_params);
			if (defs != null) {
				s.putAll(defs);
			} else {
				m.add(location, "Parameters in " + rng_params + " cannot be deduced from its length.");
				return null;
			}
		} else {
			if (size_params != 0) {
				m.add(location, "Wrong number of parameter values.");
				return null;
			}
		}

		// Find the assignment of parameters.
		List<Parameter> parameters = new ArrayList<Parameter>();
		for (ParameterExpression e : params) {
			List<Parameter> p = e.evaluate(s, m);
			if (p != null)
				parameters.addAll(p);
		}
		Iterator<Parameter> param = parameters.iterator();
		Iterator<?> value = values.iterator();

		while (param.hasNext() && value.hasNext()) {
			Parameter x = param.next();
			Object v = value.next();

			if (v instanceof Value) {
				s.put(x, (Value) v);
			} else if (v instanceof String) {
				String t = "";
				if (x.getType() instanceof TypeTag)
					t = ((TypeTag)x.getType()).toString();
				
				if (t.equals("int") || t.equals("Integer"))
					s.put(x, new IntegerValue(Integer.parseInt((String)v)));
				else if (t.equals("double") || t.equals("Double"))
					s.put(x, new DecimalValue(Double.parseDouble((String)v)));
				else if (t.equals("string") || t.equals("String"))
					s.put(x, new StringValue((String)v));
				else 
					m.add(location, "Failed to cast parameter " + s + " to " + t);
			} else {
				m.add(location, "Term " + v + " has undefined type.");
				return null;
			}
		}

		// Find the links of the interface.
		Map<Port, Port> links = new HashMap<Port, Port>();

		if (ports == null) {

			// Create a the default set of links for this interface
			List<Port> nodeslist = new ArrayList<Port>();
			for (NodeExpression e : nodes) {
				List<Port> nodes = e.evaluate(s, m);
				if (nodes != null)
					nodeslist.addAll(nodes);
			}

			Iterator<Port> node = nodeslist.iterator();

			while (node.hasNext()) {
				Port p = node.next();
				links.put(p, p);
			}

		} else {

			// Try to find the parameter value for a correct number of nodes
			int k_nodes = 0;
			NodeExpression rng_nodes = null;
			for (NodeExpression node : nodes) {
				if (node.getIndices().isEmpty())
					k_nodes++;
				for (TermExpression t : node.getIndices()) {
					if (t instanceof Range) {
						rng_nodes = node;
					} else if (t instanceof Value) {
						k_nodes += 1;
					} else if (t instanceof VariableTermExpression
							&& ((VariableTermExpression) t).evaluate(s, m) != null) {
						k_nodes += 1;
					} else {
						m.add(location, "Node " + param + " cannot have undefined indices.");
						return null;
					}
				}
			}
			int size_nodes = ports.size() - k_nodes;

			if (rng_nodes != null) {
				if(rng_nodes.evaluate(s, new Monitor())!=null){
					// This if statement evaluate a range with the parameter in the scope
				}
				else{
				Scope defs = rng_nodes.findParamFromSize(size_nodes);
				if (defs != null) {
					s.putAll(defs);
				} else {
					m.add(location, "Parameters in " + rng_nodes + " cannot be deduced from its length.");
					return null;
				}
				}
			} else {
				if (size_nodes != 0) {
					m.add(location, "Wrong number of nodes.");
					return null;
				}
			}

			List<Port> nodeslist = new ArrayList<Port>();
			for (NodeExpression e : nodes) {
				List<Port> n = e.evaluate(s, m);
				if (n != null)
					nodeslist.addAll(n);
			}

			Iterator<Port> node = nodeslist.iterator();
			Iterator<Port> port = ports.iterator();

			while (node.hasNext() && port.hasNext()) {
				Port n = node.next();
				links.put(n, port.next().join(n));
			}
		}

		return new Signature(links, s);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equalType(ParameterType other) {
		// TODO
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		ST st = new ST("<{params; separator=\", \"}>({nodes; separator=\", \"})", '{', '}');
		st.add("params", params);
		st.add("nodes", nodes);
		return st.render();
	}

}
