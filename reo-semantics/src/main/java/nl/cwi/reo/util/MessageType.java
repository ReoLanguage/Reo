package nl.cwi.reo.util;

/**
 * Enumeration of all message types.
 */
public enum MessageType {
	
	/**
	 * Information.
	 */
	INFO, 
	
	/**
	 * Compiler warning.
	 */
	WARNING, 
	
	/**
	 * Compiler error.
	 */
	ERROR;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		switch(this) {
		case INFO: return "[INFO]";
		case WARNING: return "[WARNING]";
		case ERROR: return "[ERROR]";
		default: throw new IllegalArgumentException();
		}
	}
}
