package nl.cwi.reo.interpret.connectors;

// TODO: Auto-generated Javadoc
/**
 * Enumeration of all compiler supported languages.
 */
public enum Language {

	/**
	 * Java language.
	 */
	JAVA,

	/** C11 language. */
	C11,

	/** Maude language. */
	MAUDE,

	/** PRISM language for probabilistic model checking. */
	PRISM,

	/** Proto Runtime. */
	PRT,

	/** Promela language. */
	PROMELA,

	/** Input language */
	TREO,

	/** Q# language */
	QSHARP,
	
	/** Pr text. */
	TEXT;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		switch (this) {
		case JAVA:
			return "Java";
		case C11:
			return "C11";
		case MAUDE:
			return "Maude";
		case PRISM:
			return "PRISM";
		case PRT:
			return "Proto runtime";
		case TEXT:
			return "Text";
		case TREO:
			return "Treo";
		case QSHARP:
			return "Q#";
		case PROMELA:
			return "Promela";
		default:
			throw new IllegalArgumentException();
		}
	}
}
