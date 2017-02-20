package nl.cwi.reo.interpret.values;

/**
 * Interpretation of string value.
 */
public final class StringValue implements Value {

	/**
	 * Value.
	 */
	private int x; 
	
	/**
	 * Constructs a new integer value.
	 * @param x		value
	 */
	public StringValue(int x) {
		this.x = x;
	}

	public static StringValue concat(StringValue a, StringValue b) {
		return new StringValue(a.x + b.x);
	}

}
