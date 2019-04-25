package nl.cwi.reo.runtime;
import nl.cwi.reo.components.Display;
import nl.cwi.reo.components.Engine;

import nl.cwi.reo.runtime.Input;
import nl.cwi.reo.runtime.Output;
import nl.cwi.reo.runtime.Port;

public class Workers {

	public static void Display(Port<String> whitePort, Port<String> blackPort) {
		new Display(whitePort, blackPort).run();
	}

	public static void Engine(Port<String> inputPort, Port<String> outputPort) {
		// Input port in the protocol is an output port for the engine
		new Engine(inputPort, outputPort).run();
	}

}
