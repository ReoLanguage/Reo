package nl.cwi.reo.interpret.integers;

import java.util.Map;
import java.util.Objects;

import org.antlr.v4.runtime.Token;

import nl.cwi.reo.errors.CompilationException;
import nl.cwi.reo.interpret.booleans.BooleanValue;
import nl.cwi.reo.interpret.oldstuff.Expression;

public final class IntegerValue implements IntegerExpression {
	
	/**
	 * Natural number.
	 */
	private final int n;
	
	/**
	 * Constructs a natural number from a string.
	 * @param s 	string representation of a natural number
	 */
	public IntegerValue(int n) {
		this.n = n;
	}
	
	public int toInteger() {
		return this.n;
	}

	/**
	 * Evaluates this natural number to a Integer.
	 * @param params		 	parameter assignment
	 * @return Integer evaluation with respect to parameter assignment.
	 */
	public IntegerExpression evaluate(Map<String, Expression> params) {
		return new IntegerValue(this.n);
	}
	
	public static IntegerValue addition(IntegerValue v1, IntegerValue v2) {
		return new IntegerValue(v1.n + v2.n);
	}
	
	public static IntegerValue substraction(IntegerValue v1, IntegerValue v2) {
		return new IntegerValue(v1.n - v2.n);
	}
	
	public static IntegerValue multiplication(IntegerValue v1, IntegerValue v2) {
		return new IntegerValue(v1.n * v2.n);
	}
	
	public static IntegerValue division(IntegerValue v1, IntegerValue v2, Token operator) {
		if (v2.n == 0) 
			throw new CompilationException(operator, "Cannot divide by zero.");
		return new IntegerValue(v1.n / v2.n);
	}
	
	public static IntegerValue remainder(IntegerValue v1, IntegerValue v2, Token operator) {
		if (v2.n == 0) 
			throw new CompilationException(operator, "Modulus cannot be zero.");
		return new IntegerValue(v1.n % v2.n);
	}
	
	public static IntegerValue exponentiation(IntegerValue v1, IntegerValue v2) {
		return new IntegerValue((int)Math.pow(v1.n, v2.n));
	}
	
	public static IntegerValue unarymin(IntegerValue v) {
		return new IntegerValue(-v.n);
	}

	public static BooleanValue leq(IntegerValue v1, IntegerValue v2) {
		return new BooleanValue(v1.n <= v2.n);
	}

	public static BooleanValue lt(IntegerValue v1, IntegerValue v2) {
		return new BooleanValue(v1.n < v2.n);
	}

	public static BooleanValue geq(IntegerValue v1, IntegerValue v2) {
		return new BooleanValue(v1.n >= v2.n);
	}

	public static BooleanValue gt(IntegerValue v1, IntegerValue v2) {
		return new BooleanValue(v1.n > v2.n);
	}

	public static BooleanValue eq(IntegerValue v1, IntegerValue v2) {
		return new BooleanValue(v1.n == v2.n);
	}

	public static BooleanValue neq(IntegerValue v1, IntegerValue v2) {
		return new BooleanValue(v1.n != v2.n);
	}	
	
	@Override
	public boolean equals(Object other) {
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof IntegerValue)) return false;
	    IntegerValue p = (IntegerValue)other;
	   	return Objects.equals(this.n, p.n);
	}
	
	@Override
	public int hashCode() {
	    return Objects.hash(this.n);
	}
	
	@Override
	public String toString() {
		return "" + n;
	}
}
