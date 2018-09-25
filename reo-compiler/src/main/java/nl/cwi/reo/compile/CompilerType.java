package nl.cwi.reo.compile;

// TODO: Auto-generated Javadoc
/**
 * Enumerates all implemented semantics for Reo.
 */
public enum CompilerType {

	/**
	 * Default compiler.
	 */
	DEFAULT,

	/**
	 * Default compiler.
	 */
	CH,

	/**
	 * Lykos compiler.
	 */
	LYKOS;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		switch (this) {
		case CH:
			return "CH";
		case DEFAULT:
			return "Default";
		case LYKOS:
			return "Lykos";
		default:
			throw new IllegalArgumentException();
		}
	}

}
