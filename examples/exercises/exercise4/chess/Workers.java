import nl.cwi.reo.runtime.Input;
import nl.cwi.reo.runtime.Output;
import nl.cwi.reo.runtime.Port;

public class Workers {

	public static void Display(Port<String> whitePort, Port<String> blackPort) {
		new Display(whitePort, blackPort).run();
	}

	public static void Engine(Port<String> inputPort, Port<String> outputPort) {
		new Engine(inputPort, outputPort).run();
	}

	static int N = 100;
	static int k = 100;

	public static void producer(Output<String> a) {
		for (int i = 0; i < N; i++)
			a.put("d" + i);
		System.out.println(" done ");
		System.exit(0);
	}

	public static void consumer(Input<String> a) {
		for (int i = 0; i < k * N; i++) {
			System.out.println(i + ": " + a.get() + " ");
		}
		System.out.println(" done ");
		System.exit(0);
	}

}
