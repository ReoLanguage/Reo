package nl.cwi.reo.interpret.values;

import java.util.Arrays;
import java.util.List;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.terms.Term;
import nl.cwi.reo.interpret.terms.TermExpression;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of integer value.
 */
public final class IntegerValue implements Value, TermExpression {

	/**
	 * Value.
	 */
	private int x; 
	
	/**
	 * Constructs a new integer value.
	 * @param x		value
	 */
	public IntegerValue(int x) {
		this.x = x;
	}
	
	/**
	 * Gets the value of this integer.
	 * @return gets the value
	 */
	public int getValue() {
		return x;
	}

	public static IntegerValue add(IntegerValue a, IntegerValue b) {
		return new IntegerValue(a.x + b.x);
	}

	public static IntegerValue min(IntegerValue a) {
		return new IntegerValue(-a.x);
	}

	public static IntegerValue min(IntegerValue a, IntegerValue b) {
		return new IntegerValue(a.x - b.x);
	}

	public static IntegerValue mul(IntegerValue a, IntegerValue b) {
		return new IntegerValue(a.x * b.x);
	}

	public static IntegerValue div(IntegerValue a, IntegerValue b) {
		return b.x == 0 ? null : new IntegerValue(a.x / b.x);
	}

	public static IntegerValue mod(IntegerValue a, IntegerValue b) {
		return b.x == 0 ? null :new IntegerValue(a.x % b.x);
	}

	public static IntegerValue exp(IntegerValue a, IntegerValue b) {
		return new IntegerValue((int)Math.pow(a.x, b.x));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Term> evaluate(Scope s, Monitor m) {
		return Arrays.asList(this);
	}
}
