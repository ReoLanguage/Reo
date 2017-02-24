package nl.cwi.reo.interpret.predicates;

import java.util.Arrays;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.util.Monitor;

/**
 * A predicate that represents a truth value.
 */
public class BooleanPredicate implements PredicateExpression {

	/**
	 * Boolean value
	 */
	private boolean bool;
	
	/**
	 * Constructs a new boolean predicate.
	 * @param bool	boolean value
	 */
	public BooleanPredicate(boolean bool) {
		this.bool = bool;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Predicate evaluate(Scope s, Monitor m) {
		return bool ? new Predicate(Arrays.asList(s)) : new Predicate();
	}
}
