package nl.cwi.reo.semantics.predicates;

/**
 * An atomic nullary predicate (i.e., a boolean value).
 */
public class TruthValue extends Relation {

	/**
	 * Boolean value
	 */
	private final boolean b;

	/**
	 * Constructs a boolean value from a boolean.
	 * 
	 * @param b
	 *            boolean
	 */
	public TruthValue(boolean b) {
		super(b ? "\u22A4" : "\u22A5", b ? "true" : "false", null);
		this.b = b;
	}

	/**
	 * Gets the value of this boolean
	 * 
	 * @return the boolean value of this nullary relation.
	 */
	public boolean getBool() {
		return b;
	}

}
