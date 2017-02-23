package nl.cwi.reo.interpret.predicates;

import java.util.List;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of a disjunction.
 */
public final class Disjunction implements PredicateExpression {

	/**
	 * List of disjuncts.
	 */
	private List<PredicateExpression> predicates;
	
	/**
	 * Constructs a new disjunction.
	 * @param predicates	list of disjuncts
	 */
	public Disjunction(List<PredicateExpression> predicates) {
		this.predicates = predicates;
	}

	/**
	 * Return a new Predicate with disjunction of scope
	 * and empty scope if the Term in one Predicate is not 
	 * defined in one of the others
	 *  
	 * {@inheritDoc}
	 */
	@Override
	public Predicate evaluate(Scope s, Monitor m) {
		// TODO Auto-generated method stub
		return null;
	}

}
