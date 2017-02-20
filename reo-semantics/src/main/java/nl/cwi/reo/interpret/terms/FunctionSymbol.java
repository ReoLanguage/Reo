package nl.cwi.reo.interpret.terms;

/**
 * Enumeration of all possible function symbols.
 */
public enum FunctionSymbol {
	
	/**
	 * Multiplication.
	 */
	MUL, 
	
	/**
	 * Division.
	 */
	DIV, 
	
	/**
	 * Remainder.
	 */
	MOD, 
	
	/**
	 * Addition.
	 */
	ADD, 
	
	/**
	 * Subtraction and inversion.
	 */
	MIN, 
	
	/**
	 * Exponentiation.
	 */
	POW;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		switch(this) {
		case MUL: return "*";
		case DIV: return "/";
		case MOD: return "%";
		case ADD: return "+";
		case MIN: return "-";
		case POW: return "^";
		default: throw new IllegalArgumentException();
		}
	}
}
