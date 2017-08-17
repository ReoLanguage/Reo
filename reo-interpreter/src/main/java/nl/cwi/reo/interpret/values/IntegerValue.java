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
 * Interpretation of integer value.
 */
public final class IntegerValue implements Value, TermExpression {

	/**
	 * Value.
	 */
	private final int x;

	/**
	 * Constructs a new integer value.
	 * 
	 * @param x
	 *            value
	 */
	public IntegerValue(int x) {
		this.x = x;
	}

	/**
	 * Gets the value of this integer.
	 * 
	 * @return gets the value
	 */
	public int getValue() {
		return x;
	}

	/**
	 * Adds the.
	 *
	 * @param a
	 *            the a
	 * @param b
	 *            the b
	 * @return the integer value
	 */
	public static IntegerValue add(IntegerValue a, IntegerValue b) {
		return new IntegerValue(a.x + b.x);
	}

	/**
	 * Min.
	 *
	 * @param a
	 *            the a
	 * @return the integer value
	 */
	public static IntegerValue min(IntegerValue a) {
		return new IntegerValue(-a.x);
	}

	/**
	 * Min.
	 *
	 * @param a
	 *            the a
	 * @param b
	 *            the b
	 * @return the integer value
	 */
	public static IntegerValue min(IntegerValue a, IntegerValue b) {
		return new IntegerValue(a.x - b.x);
	}

	/**
	 * Mul.
	 *
	 * @param a
	 *            the a
	 * @param b
	 *            the b
	 * @return the integer value
	 */
	public static IntegerValue mul(IntegerValue a, IntegerValue b) {
		return new IntegerValue(a.x * b.x);
	}

	/**
	 * Div.
	 *
	 * @param a
	 *            the a
	 * @param b
	 *            the b
	 * @return the integer value
	 */
	@Nullable
	public static IntegerValue div(IntegerValue a, IntegerValue b) {
		return b.x == 0 ? null : new IntegerValue(a.x / b.x);
	}

	/**
	 * Mod.
	 *
	 * @param a
	 *            the a
	 * @param b
	 *            the b
	 * @return the integer value
	 */
	@Nullable
	public static IntegerValue mod(IntegerValue a, IntegerValue b) {
		return b.x == 0 ? null : new IntegerValue(a.x % b.x);
	}

	/**
	 * Exp.
	 *
	 * @param a
	 *            the a
	 * @param b
	 *            the b
	 * @return the integer value
	 */
	public static IntegerValue exp(IntegerValue a, IntegerValue b) {
		return new IntegerValue((int) Math.pow(a.x, b.x));
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
		return Integer.toString(x);
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
		if (!(other instanceof IntegerValue))
			return false;
		IntegerValue p = (IntegerValue) other;
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
