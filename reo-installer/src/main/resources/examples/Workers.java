import nl.cwi.reo.runtime.java.Input;
import nl.cwi.reo.runtime.java.Output;

public class Workers {

	static int N = 100;
	static int k = 10;
	
	public static void producer(Output<String> a) {
		int i = 0;
		while(true){
			a.put("d" + i);
			i++;
		}
	}
	
	public static void consumer(Input<String> a) {
		for (int i = 0; i < k*N; i++){
			System.out.println( i + ": " + a.get() + " ");
		}
		System.out.println(" done ");
		System.exit(0);
	}
}
