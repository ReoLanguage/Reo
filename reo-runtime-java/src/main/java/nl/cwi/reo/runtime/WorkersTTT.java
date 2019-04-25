package nl.cwi.reo.runtime;
import nl.cwi.reo.components.Visual;


import nl.cwi.reo.components.Cell;

import nl.cwi.reo.runtime.Port;

public class WorkersTTT {

	public static void Display(Port<String> cell) {
		new Visual(cell).run();
	}

	public static void Engine(Port<String> inputPort, Port<String> outputPort) {
		// Input port in the protocol is an output port for the engine
		new Cell(inputPort, outputPort).run();
	}

}
