package nl.cwi.reo.interpret.values;

/**
 * Interpretation of integer value.
 */
public final class IntegerValue implements Value {

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
}
