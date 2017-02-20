package nl.cwi.pr.runtime;

import nl.cwi.pr.runtime.api.InputPort;
import nl.cwi.pr.runtime.api.OutputPort;

public class Ports {

	public static InputPort newInputPort() {
		return new InputPortImpl();
	}

	public static OutputPort newOutputPort() {
		return new OutputPortImpl();
	}
}
