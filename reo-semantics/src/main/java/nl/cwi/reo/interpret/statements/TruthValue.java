package nl.cwi.reo.interpret.statements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of a predicate that represents a truth value.
 */
public class TruthValue implements PredicateExpression {

	/**
	 * Boolean value
	 */
	private boolean bool;
	
	/**
	 * Constructs a new boolean predicate.
	 * @param bool	boolean value
	 */
	public TruthValue(boolean bool) {
		this.bool = bool;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Scope> evaluate(Scope s, Monitor m) {
		return bool ? Arrays.asList(s) : new ArrayList<Scope>();
	}
}
