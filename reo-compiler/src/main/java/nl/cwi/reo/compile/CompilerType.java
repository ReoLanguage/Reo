package nl.cwi.reo.compile;

/**
 * Enumerates all implemented semantics for Reo.
 */
public enum CompilerType {
	
	/**
	 * Lykos compiler.
	 */
	LYKOS, 
	
	/**
	 * Compiler based on Implication Hypergraphs.
	 */
	IHC;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		switch(this) {
		case LYKOS: return "Lykos";
		case IHC: return "IHC";
		default: throw new IllegalArgumentException();
		}
	}

}
