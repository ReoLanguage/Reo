package nl.cwi.reo.compile;

/**
 * Enumerates all implemented semantics for Reo.
 */
public enum CompilerType {

	
	/**
	 * Default compiler.
	 */
	DEFAULT,
	
	/**
	 * Lykos compiler.
	 */
	LYKOS;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		switch(this) {
		case DEFAULT: return "Default";
		case LYKOS: return "Lykos";
		default: throw new IllegalArgumentException();
		}
	}

}
