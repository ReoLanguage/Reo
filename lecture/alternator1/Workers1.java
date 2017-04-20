import nl.cwi.pr.runtime.api.InputPort;
import nl.cwi.pr.runtime.api.OutputPort;

public class Workers1 {

	public static void producer(OutputPort a) {
		for (int i = 0; i < 10; i++)
			a.putUninterruptibly("d" + i);
	}

	public static void consumer(InputPort a) {
		for (int i = 0; i < 300; i++)
			System.out.print(i + ": " + a.getUninterruptibly() + " ");
		System.exit(0);
	}
}
