package nl.cwi.reo.interpret.predicates;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.terms.ListExpression;
import nl.cwi.reo.interpret.variables.VariableExpression;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of a bounded universal quantification of a Reo predicate over a finite list.
 */
public final class Universal implements PredicateExpression {
	
	/**
	 * Quantified variable.
	 */
	private final VariableExpression var;
	
	/**
	 * Domain of the quantified variable.
	 */
	private final ListExpression list; 
	
	/**
	 * Quantified predicate.
	 */
	private final PredicateExpression predicate;
	
	/**
	 * Constructs a new bounded universal quantification.
	 * @param var		quantified variable
	 * @param list		domain of quantification
	 * @param predicate	quantified predicate
	 */
	public Universal(VariableExpression var, ListExpression list, PredicateExpression predicate) {
		this.var = var;
		this.list = list;
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
