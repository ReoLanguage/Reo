package nl.cwi.reo.interpret.values;

import java.util.Arrays;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.terms.Terms;
import nl.cwi.reo.interpret.terms.TermsExpression;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of decimal value.
 */
public final class DecimalValue implements Value, TermsExpression {
	
	/**
	 * Value.
	 */
	private double x; 
	
	/**
	 * Constructs a new integer value.
	 * @param x		value
	 */
	public DecimalValue(double x) {
		this.x = x;
	}
	
	/**
	 * Gets the value of this decimal.
	 * @return gets the value
	 */
	public double getValue() {
		return x;
	}

	public static DecimalValue add(DecimalValue a, DecimalValue b) {
		return new DecimalValue(a.x + b.x);
	}

	public static DecimalValue min(DecimalValue a) {
		return new DecimalValue(-a.x);
	}

	public static DecimalValue min(DecimalValue a, DecimalValue b) {
		return new DecimalValue(a.x - b.x);
	}

	public static DecimalValue mul(DecimalValue a, DecimalValue b) {
		return new DecimalValue(a.x * b.x);
	}

	public static DecimalValue div(DecimalValue a, DecimalValue b) {
		return b.x == 0 ? null : new DecimalValue(a.x / b.x);
	}

	public static DecimalValue exp(DecimalValue a, DecimalValue b) {
		return new DecimalValue(Math.pow(a.x, b.x));
	}

	@Override
	public Terms evaluate(Scope s, Monitor m) {
		return new Terms(Arrays.asList(this));
	}
}
