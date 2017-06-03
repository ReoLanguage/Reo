import nl.cwi.reo.runtime.java.Input;
import nl.cwi.reo.runtime.java.Output;

public class Workers2 {

	static int N = 100;
	static int k = 11;
	
	public static void producer(Output<String> a) {
		for (int i = 0; i < N; i++)
			a.put("d" + i);
	}
	
	public static void consumer(Input<String> a) {
		for (int i = 0; i < k*N; i++){
			a.get();
			System.out.println( i + ": " + a.get() + " ");
		}
		System.out.println(" done ");
		System.exit(0);
	}
}
