package nl.cwi.reo.interpret.statements;

/**
 * Enumeration of all possible relation symbols.
 */
public enum RelationSymbol {
	LT, LEQ, EQ, NEQ, GEQ, GT;

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
			return null;
		}
	}
}
