package nl.cwi.reo.interpret.predicates;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of predicate negation.
 */
public final class Negation implements PredicateExpression {
	
	/**
	 * Negated predicate.
	 */
	private final PredicateExpression predicate;

	/**
	 * Constructs a predicate negation.
	 * @param predicate 	negated predicate
	 */
	public Negation(PredicateExpression predicate) {
		this.predicate = predicate;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Predicate evaluate(Scope s, Monitor m) {
		// TODO Auto-generated method stub
		return null;
	}

}
