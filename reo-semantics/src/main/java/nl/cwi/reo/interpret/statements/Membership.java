package nl.cwi.reo.interpret.statements;

import java.util.ArrayList;
import java.util.List;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.terms.ListExpression;
import nl.cwi.reo.interpret.terms.Term;
import nl.cwi.reo.interpret.values.Value;
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
	 * Gets the variable.
	 * @return identifier
	 */
	public Identifier getVariable() {
		return x;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Nullable
	public List<Scope> evaluate(Scope s, Monitor m) {
		List<Scope> scopes = new ArrayList<Scope>();
		List<Term> terms = list.evaluate(s, m);
		if (terms == null) return null;
		for (Term t : terms) 
			if (t instanceof Value) scopes.add(s.extend(x, (Value)t)); else return null;
		return scopes;
	}

}
