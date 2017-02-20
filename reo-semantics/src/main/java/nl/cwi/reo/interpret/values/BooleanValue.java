package nl.cwi.reo.interpret.values;

/**
 * Interpretation of boolean value.
 */
public final class BooleanValue implements Value {
	
	/**
	 * Value.
	 */
	private boolean x; 
	
	/**
	 * Constructs a new integer value.
	 * @param x		value
	 */
	public BooleanValue(boolean x) {
		this.x = x;
	}
	
	/**
	 * Gets the value of this boolean.
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
}
