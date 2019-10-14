import nl.cwi.reo.runtime.Input;
import nl.cwi.reo.runtime.Output;

public class Processes {

	public static void Red(Output<String> port) {
		while (true) {
			for (int i = 0; i < 30000000; ++i);
			String datum = "Hello, ";
			port.put(datum);
		}
	}

	public static void Green(Output<String> port) {
		while (true) {
			for (int i = 0; i < 50000000; ++i);
			String datum = "world! ";
			port.put(datum);
		}
	}

	public static void Blue(Input<String> port) {
		for (int k = 0; k < 10; ++k) {
			for (int i = 0; i < 40000000; ++i);
			String datum = port.get();
			System.out.print(datum);
		}
		System.exit(0);
	}
}
