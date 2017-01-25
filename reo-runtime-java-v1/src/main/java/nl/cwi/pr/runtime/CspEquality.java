package nl.cwi.pr.runtime;

public abstract class CspEquality extends CspLiteral {

	//
	// FIELDS
	//

	private final CspVariable lhsVariable;
	private final CspVariable rhsVariable;

	//
	// CONSTRUCTORS
	//

	public CspEquality(final CspVariable lhsVariable,
			final CspVariable rhsVariable, final CspVariable... variables) {

		super(variables);
		this.lhsVariable = lhsVariable;
		this.rhsVariable = rhsVariable;
	}

	//
	// METHODS
	//

	protected boolean close() {
		if (isClosed())
			return true;

		if (lhsVariable != null && lhsVariable.value == null)
			try {
				lhsVariable.value = rhs();
			} catch (final NullPointerException exc) {
			}

		if (rhsVariable != null && rhsVariable.value == null)
			try {
				rhsVariable.value = lhs();
			} catch (final NullPointerException exc) {
			}

		return isClosed();
	}

	@Override
	protected boolean holds() {
		try {
			return lhs().equals(rhs());
		}

		catch (Exception exc) {
			return false;
		}
	}

	protected abstract Object lhs();

	protected abstract Object rhs();
}
