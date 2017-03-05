package nl.cwi.reo.interpret.values;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.terms.Term;
import nl.cwi.reo.interpret.terms.TermExpression;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of boolean value.
 */
public final class BooleanValue implements Value, TermExpression {

	/**
	 * Value.
	 */
	private boolean x;

	/**
	 * Constructs a new integer value.
	 * 
	 * @param x
	 *            value
	 */
	public BooleanValue(boolean x) {
		this.x = x;
	}

	/**
	 * Gets the value of this boolean.
	 * 
	 * @return gets the value
	 */
	public boolean getValue() {
		return x;
	}

	public static BooleanValue and(BooleanValue a, BooleanValue b) {
		return new BooleanValue(a.x && b.x);
	}

	public static BooleanValue or(BooleanValue a, BooleanValue b) {
		return new BooleanValue(a.x || b.x);
	}

	public static BooleanValue not(BooleanValue a) {
		return new BooleanValue(!a.x);
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
	public List<Term> evaluate(Scope s, Monitor m) {
		return Arrays.asList(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return Boolean.toString(x);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (other == this)
			return true;
		if (!(other instanceof BooleanValue))
			return false;
		BooleanValue p = (BooleanValue) other;
		return Objects.equals(this.x, p.x);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.x);
	}

}
