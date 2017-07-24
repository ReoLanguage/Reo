package nl.cwi.reo.interpret.statements;

// TODO: Auto-generated Javadoc
/**
 * Enumeration of all possible relation symbols.
 */
public enum RelationSymbol {

	/** The lt. */
	LT,
	/** The leq. */
	LEQ,
	/** The eq. */
	EQ,
	/** The neq. */
	NEQ,
	/** The geq. */
	GEQ,
	/** The gt. */
	GT;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		switch (this) {
		case LT:
			return "<";
		case LEQ:
			return "<=";
		case EQ:
			return "=";
		case NEQ:
			return "!=";
		case GEQ:
			return ">=";
		case GT:
			return ">";
		default:
			return "error";
		}
	}
}
