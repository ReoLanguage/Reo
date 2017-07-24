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
 * Interpretation of string value.
 */
public final class StringValue implements Value, TermExpression {

	/**
	 * Value.
	 */
	private String x;

	/**
	 * Constructs a new string value.
	 * 
	 * @param x
	 *            value
	 */
	public StringValue(String x) {
		this.x = x;
	}

	/**
	 * Gets the value of this string.
	 * 
	 * @return gets the value
	 */
	public String getValue() {
		return x;
	}

	/**
	 * Concat.
	 *
	 * @param a
	 *            the a
	 * @param b
	 *            the b
	 * @return the string value
	 */
	public static StringValue concat(StringValue a, StringValue b) {
		return new StringValue(a.x + b.x);
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
		return x;
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
		if (!(other instanceof StringValue))
			return false;
		StringValue p = (StringValue) other;
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
