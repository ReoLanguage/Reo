package nl.cwi.reo.interpret.statements;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of a bounded existential quantification of a Reo predicate
 * over a finite list.
 */
public final class Existential implements PredicateExpression {

	/**
	 * Quantified variable and domain.
	 */
	private final Membership membership;

	/**
	 * Quantified predicate.
	 */
	private final PredicateExpression predicate;

	/**
	 * Constructs a new bounded existential quantification.
	 * 
	 * @param membership
	 *            quantified variable and domain
	 * @param predicate
	 *            quantified predicate
	 */
	public Existential(Membership membership, PredicateExpression predicate) {
		this.membership = membership;
		this.predicate = predicate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Nullable
	public List<Scope> evaluate(Scope s, Monitor m) {
		List<Scope> scopes = new ArrayList<Scope>();
		List<Scope> list = membership.evaluate(s, m);
		if (list == null)
			return null;
		for (Scope si : list)
			scopes.addAll(predicate.evaluate(si, m));
		for (Scope si : scopes)
			si.remove(membership.getVariable());
		return scopes;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Identifier> getVariables() {
		Set<Identifier> vars = new HashSet<Identifier>();
		vars.addAll(predicate.getVariables());
		vars.remove(membership.getVariable());
		return vars;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "exists " + membership + "(" + predicate + ")";
	}

}
