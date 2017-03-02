package nl.cwi.reo.interpret.statements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.checkerframework.checker.nullness.qual.Nullable;

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
	@Nullable
	public List<Scope> evaluate(Scope s, Monitor m) {
		return predicate.evaluate(s, m).isEmpty() ? Arrays.asList(s) : new ArrayList<Scope>();
	}

}
