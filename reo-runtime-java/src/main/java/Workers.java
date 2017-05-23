import nl.cwi.reo.runtime.java.Port;

public class Workers {

	public static void Display(Port<String> whitePort, Port<String> blackPort) {
		new Display(whitePort, blackPort).run();
	}

	public static void Engine(Port<String> inputPort, Port<String> outputPort) {
		//Input port in the protocol is an output port for the engine
		new Engine(outputPort, inputPort).run();
	}
}
