package nl.cwi.reo.semantics.predicates;

// TODO: Auto-generated Javadoc
/**
 * Constant that represents absence of data. This value is used to encode
 * synchronization constraints and empty memory cells.
 */
public class NullValue extends Function {

	/**
	 * Constructs an null value.
	 */
	public NullValue() {
		super("*", "null", null);
	}

}
