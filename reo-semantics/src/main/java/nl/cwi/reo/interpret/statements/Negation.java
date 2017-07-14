package nl.cwi.reo.interpret.statements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.variables.Identifier;
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
	 * 
	 * @param predicate
	 *            negated predicate
	 */
	public Negation(PredicateExpression predicate) {
		this.predicate = predicate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Nullable
	public List<Scope> evaluate(Scope s, Monitor m) {
		List<Scope> list = predicate.evaluate(s, m);
		if (list == null)
			return null;
		// TODO is this correct?
		return list.isEmpty() ? Arrays.asList(s) : new ArrayList<Scope>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Identifier> getVariables() {
		return predicate.getVariables();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "!(" + predicate + ")";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Nullable
	public Set<Identifier> getDefinedVariables(Set<Identifier> defns) {
		Set<Identifier> diff = predicate.getDefinedVariables(defns);
		if (diff == null)
			return null;
		diff.removeAll(defns);
		return diff.isEmpty() ? defns : null;
	}

}
