package nl.cwi.reo.interpret.signatures;

import java.util.List;

import nl.cwi.reo.interpret.nodes.NodeListExpression;
import nl.cwi.reo.interpret.parameters.ParameterListExpression;
import nl.cwi.reo.interpret.parameters.ParameterType;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.terms.Term;
import nl.cwi.reo.util.Location;

/**
 * Interpretation of a component signature.
 */
public final class SignatureExpression implements ParameterType {
	
	/**
	 * List of parameters.
	 */
	private final ParameterListExpression params;
	
	/**
	 * List of nodes.
	 */
	private final NodeListExpression nodes;
	
	/**
	 * Location of this signature in Reo source file.
	 */
	private final Location location;

	/**
	 * Constructs a new signature expression.
	 * @param params		list of parameters
	 * @param nodes			list of nodes
	 * @param location		location of signature in Reo source file.
	 */
	public SignatureExpression(ParameterListExpression params, NodeListExpression nodes, Location location) {
		this.params = params;
		this.nodes = nodes;
		this.location = location;
	}

	/**
	 * Evaluates this interface for a given list of parameter values
	 * and a given list of ports.
	 * @param values	parameter values
	 * @param ports		ports in interface
	 * @return signature that contains interface renaming and parameter assignments.
	 */
	public Signature evaluate(List<Term> values, List<Port> ports) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equalType(ParameterType other) {
		// TODO Auto-generated method stub
		return false;
	}

}
