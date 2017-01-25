package nl.cwi.pr.runtime;

public abstract class CspLiteral {

	//
	// FIELDS
	//
	
	private final CspVariable[] variables;
	volatile boolean check;
	
	//
	// CONSTRUCTORS
	//	

	protected CspLiteral(final CspVariable... variables) {
		this.variables = variables;
		for (final CspVariable v : this.variables)
			v.literals.add(this);
	}
	
	//
	// METHODS
	//

	protected boolean close() {
		return isClosed();
	}

	protected boolean isClosed() {
		for (final CspVariable v : variables)
			if (v.value == null)
				return false;

		return true;
	}

	protected abstract boolean holds();
}