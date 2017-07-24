package nl.cwi.reo.semantics;

// TODO: Auto-generated Javadoc
/**
 * Enumerates all implemented semantics for Reo.
 */
public enum SemanticsType {

	/**
	 * Constraint automata with memory.
	 */
	CAM,

	/**
	 * Constraint hypergraphs.
	 */
	CH,

	/**
	 * Predicates.
	 */
	P,

	/**
	 * Port automata.
	 */
	PA,

	/**
	 * Plain semantics (for testing purposes).
	 */
	PLAIN,

	/**
	 * Atomic components for Lykos compiler by Sung-Shik Jongmans.
	 */
	PR,

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
		switch (this) {
		case CAM:
			return "cam";
		case CH:
			return "rba"; // TODO fix this
		case P:
			return "p";
		case PA:
			return "pa";
		case PLAIN:
			return "plain";
		case PR:
			return "pr";
		case SA:
			return "sa";
		case WA:
			return "wa";
		default:
			throw new IllegalArgumentException();
		}
	}

}
