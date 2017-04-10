package nl.cwi.reo.semantics;

/**
 * Enumerates all implemented semantics for Reo.
 */
public enum SemanticsType {
	
	/**
	 * Constraint automata with memory.
	 */
	CAM, 
	
	/**
	 * Predicates.
	 */
	P,
	
	/**
	 * Port automata.
	 */
	PA,
	
	/**
	 * Plain semantics.
	 */
	PLAIN, 
	
	/**
	 * Atomic components for Lykos compiler by Sung-Shik Jongmans.
	 */
	PR, 
	
	/**
	 * Rule Based automata.
	 */
	RBA,
	
	/**
	 * Seepage automata.
	 */
	SA,
	
	/**
	 * Work automata.
	 */
	WA;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		switch(this) {
		case CAM: return "cam";
		case P: return "p";
		case PA: return "pa";
		case PLAIN: return "plain";
		case PR: return "pr";
		case RBA: return "rba";
		case SA: return "sa";
		case WA: return "wa";
		default: throw new IllegalArgumentException();
		}
	}

}
