package nl.cwi.reo.interpret.predicates;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.terms.ListExpression;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of a membership of a finite list.
 */
public final class Membership implements PredicateExpression {

	/**
	 * Variable.
	 */
	private final Identifier x;
	
	/**
	 * List of terms.
	 */
	private final ListExpression list;
	
	/**
	 * Constructs a new membership predicate.
	 * @param x		variable
	 * @param list	list of terms
	 */
	public Membership(Identifier x, ListExpression list) {
		this.x = x;
		this.list = list;
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
