import nl.cwi.reo.runtime.java.Input;
import nl.cwi.reo.runtime.java.Output;

public class Workers2 {

	public static void producer(Output<String> a) {
		for (int i = 0; i < 10; i++)
			a.put("d" + i);
	}
	
	public static void consumer(Input<String> a) {
		for (int i = 0; i < 300; i++)
			System.out.println(i + ": " + a.get() + " ");
		System.exit(0);
	}
}
