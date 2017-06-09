package nl.cwi.reo.components;
import nl.cwi.reo.runtime.Input;
import nl.cwi.reo.runtime.Output;

public class Workers2 {

	static int N = 100;
	static int k = 100;
	
	public static void producer(Output<String> a) {
		for (int i = 0; i < N; i++)
			a.put("d" + i);
		System.out.println(" done ");
		System.exit(0);
	}
	
	public static void consumer(Input<String> a) {
		for (int i = 0; i < k*N; i++){
			System.out.println( i + ": " + a.get() + " ");
		}
		System.out.println(" done ");
		System.exit(0);
	}
}
