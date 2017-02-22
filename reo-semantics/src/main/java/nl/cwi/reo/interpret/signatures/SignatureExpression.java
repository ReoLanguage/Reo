package nl.cwi.reo.interpret.signatures;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.parameters.ParameterType;
import nl.cwi.reo.interpret.terms.TermList;
import nl.cwi.reo.interpret.variables.VariableListExpression;
import nl.cwi.reo.util.Location;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of a component signature.
 */
public final class SignatureExpression implements ParameterType {
	
	/**
	 * List of parameters.
	 */
	private final VariableListExpression params;
	
	/**
	 * List of nodes.
	 */
	private final VariableListExpression nodes;
	
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
	public SignatureExpression(VariableListExpression params, VariableListExpression nodes, Location location) {
		this.params = params;
		this.nodes = nodes;
		this.location = location;
	}

	/**
	 * Evaluates this interface for a given list of parameter values
	 * and a given list of ports.
	 * @param Scope			scope needed for interpretation
	 * @param Monitor		monitor for messages
	 * @return signature that contains interface renaming and parameter assignments.
	 */
	public Signature evaluate(Scope s, Monitor m) {
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
