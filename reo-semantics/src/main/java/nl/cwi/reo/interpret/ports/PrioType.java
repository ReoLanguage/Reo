package nl.cwi.reo.interpret.ports;

/**
 * Enumerates all possible priority types of an input port.
 */
public enum PrioType {

	/**
	 * Propagates seepage to output ports coincident at the 
	 * node, only if all other input ports coincident at the 
	 * node of this input port with this ampersand label want 
	 * to propagate seepage.
	 */
	AMPERSANT, 
	
	/**
	 * Propages seepage into the component of this input port
	 * also if any input port coincident at the node propagetes
	 * seepage into the node.
	 */
	PLUS, 
	
	/**
	 * Default input port behaviour.
	 */
	NONE;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		switch(this) {
		case AMPERSANT: return "&";
		case PLUS: return "+";
		case NONE: return "";
		default: throw new IllegalArgumentException();
		}
	}
}
