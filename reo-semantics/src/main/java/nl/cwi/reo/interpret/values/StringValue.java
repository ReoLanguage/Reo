package nl.cwi.reo.interpret.values;

import java.util.Arrays;
import java.util.List;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.terms.Term;
import nl.cwi.reo.interpret.terms.TermExpression;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of string value.
 */
public final class StringValue implements Value , TermExpression {

	/**
	 * Value.
	 */
	private String x; 
	
	/**
	 * Constructs a new integer value.
	 * @param x		value
	 */
	public StringValue(String x) {
		this.x = x;
	}
	
	/**
	 * Gets the value of this string.
	 * @return gets the value
	 */
	public String getValue() {
		return x;
	}

	public static StringValue concat(StringValue a, StringValue b) {
		return new StringValue(a.x + b.x);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Term> evaluate(Scope s, Monitor m) {
		return Arrays.asList(this);
	}

}
