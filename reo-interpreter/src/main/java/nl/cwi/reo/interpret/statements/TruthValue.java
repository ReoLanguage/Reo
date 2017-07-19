package nl.cwi.reo.interpret.statements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.util.Monitor;

// TODO: Auto-generated Javadoc
/**
 * Interpretation of a predicate that represents a truth value.
 */
public class TruthValue implements PredicateExpression {

	/** Boolean value. */
	private boolean bool;

	/**
	 * Constructs a new boolean predicate.
	 * 
	 * @param bool
	 *            boolean value
	 */
	public TruthValue(boolean bool) {
		this.bool = bool;
	}

	/**
	 * Gets the boolean value of the truth value.
	 * 
	 * @return boolean value of this truth value.
	 */
	public boolean getBool() {
		return bool;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Nullable
	public List<Scope> evaluate(Scope s, Monitor m) {
		return bool ? Arrays.asList(s) : new ArrayList<Scope>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Identifier> getVariables() {
		return new HashSet<Identifier>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return Boolean.toString(bool);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Identifier> getDefinedVariables(Set<Identifier> defns) {
		return defns;
	}
}
