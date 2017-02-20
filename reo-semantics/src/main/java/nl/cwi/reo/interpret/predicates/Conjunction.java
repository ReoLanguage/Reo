package nl.cwi.reo.interpret.predicates;

import java.util.List;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of a conjunction.
 */
public final class Conjunction implements PredicateExpression {

	/**
	 * List of conjuncts.
	 */
	private List<PredicateExpression> predicates;
	
	/**
	 * Constructs a new conjunction.
	 * @param predicates 	list of conjuncts.
	 */
	public Conjunction(List<PredicateExpression> predicates) {
		this.predicates = predicates;
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
