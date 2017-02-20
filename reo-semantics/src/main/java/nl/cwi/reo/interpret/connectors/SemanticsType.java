package nl.cwi.reo.interpret.connectors;

/**
 * Enumerates all implemented semantics for Reo.
 */
public enum SemanticsType {
	
	/**
	 * Port automata.
	 */
	PA, 
	
	/**
	 * Constraint automata with memory.
	 */
	CAM, 
	
	/**
	 * Work automata.
	 */
	WA, 
	
	/**
	 * Seepage automata.
	 */
	SA, 
	
	/**
	 * Atomic components for Lykos compiler by Sung-Shik Jongmans.
	 */
	PR;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		switch(this) {
		case PA: return "pa";
		case PR: return "pr";
		case CAM: return "cam";
		case WA: return "wa";
		case SA: return "sa";
		default: throw new IllegalArgumentException();
		}
	}

}
