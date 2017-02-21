package nl.cwi.reo.interpret.values;

import java.util.Arrays;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.terms.Terms;
import nl.cwi.reo.interpret.terms.TermsExpression;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of string value.
 */
public final class StringValue implements Value , TermsExpression {

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

	@Override
	public Terms evaluate(Scope s, Monitor m) {
		// TODO Auto-generated method stub
		return new Terms(Arrays.asList(this));
	}

}
