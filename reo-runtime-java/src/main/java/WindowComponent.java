import nl.cwi.reo.runtime.java.Input;
import nl.cwi.reo.runtime.java.Output;

public class WindowComponent {
	
	
	public static void produce(Output<Integer> A) {
		
	}
	
	public static void consume(Input<Integer> A) {
			A.get();
	}

}
