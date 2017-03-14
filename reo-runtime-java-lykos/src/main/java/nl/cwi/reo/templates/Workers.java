package nl.cwi.reo.templates;

import nl.cwi.pr.runtime.api.InputPort;
import nl.cwi.pr.runtime.api.OutputPort;

public class Workers {

	public static void Display(InputPort whitePort, InputPort blackPort) {
		new Display(whitePort, blackPort).run();
	}

	public static void Engine(InputPort inputPort, OutputPort outputPort) {
		new Engine(inputPort, outputPort).run();
	}
}
