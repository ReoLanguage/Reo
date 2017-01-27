package nl.cwi.pr.autom;

import nl.cwi.pr.misc.IdObjectSpec;
import nl.cwi.pr.misc.PortFactory.PortSet;

public class AutomatonSpec implements IdObjectSpec {
	private final PortSet allPorts;
	private final String description;
	private final PortSet inputPorts;
	private final PortSet outputPorts;

	//
	// CONSTRUCTORS
	//

	public AutomatonSpec(PortSet allPorts, PortSet inputPorts,
			PortSet outputPorts, String description) {

		if (allPorts == null)
			throw new NullPointerException();
		if (inputPorts == null)
			throw new NullPointerException();
		if (outputPorts == null)
			throw new NullPointerException();
		if (description == null)
			throw new NullPointerException();

		this.allPorts = allPorts;
		this.description = description;
		this.inputPorts = inputPorts;
		this.outputPorts = outputPorts;
	}

	//
	// METHODS - PUBLIC
	//

	public PortSet getAllPorts() {
		return allPorts;
	}

	public String getDescription() {
		return description;
	}

	public PortSet getInputPorts() {
		return inputPorts;
	}

	public PortSet getOutputPorts() {
		return outputPorts;
	}

	@Override
	public String toString() {
		return description;
	}
}