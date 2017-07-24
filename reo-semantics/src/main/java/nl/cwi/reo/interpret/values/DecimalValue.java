package nl.cwi.reo.interpret.values;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.terms.Term;
import nl.cwi.reo.interpret.terms.TermExpression;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.util.Monitor;

// TODO: Auto-generated Javadoc
/**
 * Interpretation of decimal value.
 */
public final class DecimalValue implements Value, TermExpression {

	/**
	 * Value.
	 */
	private double x;

	/**
	 * Constructs a new integer value.
	 * 
	 * @param x
	 *            value
	 */
	public DecimalValue(double x) {
		this.x = x;
	}

	/**
	 * Gets the value of this decimal.
	 * 
	 * @return gets the value
	 */
	public double getValue() {
		return x;
	}

	/**
	 * Adds the.
	 *
	 * @param a
	 *            the a
	 * @param b
	 *            the b
	 * @return the decimal value
	 */
	public static DecimalValue add(DecimalValue a, DecimalValue b) {
		return new DecimalValue(a.x + b.x);
	}

	/**
	 * Min.
	 *
	 * @param a
	 *            the a
	 * @return the decimal value
	 */
	public static DecimalValue min(DecimalValue a) {
		return new DecimalValue(-a.x);
	}

	/**
	 * Min.
	 *
	 * @param a
	 *            the a
	 * @param b
	 *            the b
	 * @return the decimal value
	 */
	public static DecimalValue min(DecimalValue a, DecimalValue b) {
		return new DecimalValue(a.x - b.x);
	}

	/**
	 * Mul.
	 *
	 * @param a
	 *            the a
	 * @param b
	 *            the b
	 * @return the decimal value
	 */
	public static DecimalValue mul(DecimalValue a, DecimalValue b) {
		return new DecimalValue(a.x * b.x);
	}

	/**
	 * Div.
	 *
	 * @param a
	 *            the a
	 * @param b
	 *            the b
	 * @return the decimal value
	 */
	@Nullable
	public static DecimalValue div(DecimalValue a, DecimalValue b) {
		return b.x == 0 ? null : new DecimalValue(a.x / b.x);
	}

	/**
	 * Exp.
	 *
	 * @param a
	 *            the a
	 * @param b
	 *            the b
	 * @return the decimal value
	 */
	public static DecimalValue exp(DecimalValue a, DecimalValue b) {
		return new DecimalValue(Math.pow(a.x, b.x));
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
		return Double.toString(x);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(@Nullable Object other) {
		if (other == null)
			return false;
		if (other == this)
			return true;
		if (!(other instanceof DecimalValue))
			return false;
		DecimalValue p = (DecimalValue) other;
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
