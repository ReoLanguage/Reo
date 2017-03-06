import nl.cwi.pr.runtime.api.InputPort;
import nl.cwi.pr.runtime.api.OutputPort;

public class Processes {

	public static void Red(OutputPort port) {
		while (true) {
			for (int i = 0; i < 30000000; ++i);
			Object datum = "Hello, ";
			port.putUninterruptibly(datum);
		}
	}

	public static void Green(OutputPort port) {
		while (true) {
			for (int i = 0; i < 50000000; ++i);
			Object datum = "world!";
			port.putUninterruptibly(datum);
		}
	}

	public static void Blue(InputPort port) {
		for (int k = 0; k < 10; ++k) {
      for (int i = 0; i < 40000000; ++i);
			Object datum = port.getUninterruptibly();
			System.out.println(datum);
		}
	}
}

