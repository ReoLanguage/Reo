package nl.cwi.reo.interpret.ports;

// TODO: Auto-generated Javadoc
/**
 * Enumeration of all possible IO types of a port.
 */
public enum PortType {

	/**
	 * Input port.
	 */
	IN,

	/**
	 * Output port.
	 */
	OUT,

	/**
	 * Unknown type.
	 */
	NONE;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		switch (this) {
		case IN:
			return "?";
		case OUT:
			return "!";
		case NONE:
			return ":";
		default:
			throw new IllegalArgumentException();
		}
	}
}
