package nl.cwi.pr.runtime;

public class CspPortVariable extends CspVariable {

	//
	// FIELDS
	//

	private PortImpl port;

	//
	// CONSTRUCTORS
	//

	public CspPortVariable(final PortImpl port) {
		this.port = port;
	}

	//
	// METHODS
	//

	@Override
	public void exportValue() {
		port.buffer = value == null ? new Object() : value;
	}

	@Override
	public void importValue() {
		value = port.buffer;
	}
	
	public void resetPortBuffer() {
		port.buffer = null;
	}

	public void setPort(final PortImpl port) {
		this.port = port;
	}
}