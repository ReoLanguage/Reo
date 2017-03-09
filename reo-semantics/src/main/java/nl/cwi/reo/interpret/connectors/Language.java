package nl.cwi.reo.interpret.connectors;

/**
 * Enumeration of all compiler supported languages.
 */
public enum Language {
	
	/**
	 * Java language.
	 */
	JAVA, 
	
	/**
	 * C11 language
	 */
	C11,
	
	/**
	 * Proto Runtime
	 */
	PRT, 
	
	/**
	 * Pr text
	 */
	TEXT;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		switch(this) {
		case JAVA: return "Java";
		case C11: return "C11";
		case PRT: return "Proto runtime";
		case TEXT: return "Text";
		default: throw new IllegalArgumentException();
		}
	}
}
