package nl.cwi.reo.interpret.predicates;

import java.util.List;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.terms.ListExpression;
import nl.cwi.reo.interpret.terms.Term;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.interpret.variables.VariableExpression;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of a bounded existential quantification of a Reo predicate over a finite list.
 */
public final class Existential implements PredicateExpression {
	
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
	 * Constructs a new bounded existential quantification.
	 * @param var		quantified variable
	 * @param list		domain of quantification
	 * @param predicate	quantified predicate
	 */
	public Existential(VariableExpression var, ListExpression list, PredicateExpression predicate) {
		this.var = var;
		this.list = list;
		this.predicate = predicate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Predicate evaluate(Scope s, Monitor m) {
		List<? extends Identifier> ids = var.evaluate(s, m);
		if (ids == null || ids.size() != 1) 
			return null;
		Identifier x = ids.get(0);
		List<Term> terms = list.evaluate(s, m);
//		for (Term t : terms)
//			if (t)
		return null;
	}

}
