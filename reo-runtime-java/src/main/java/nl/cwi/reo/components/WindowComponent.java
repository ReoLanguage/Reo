package nl.cwi.reo.components;
import nl.cwi.reo.runtime.Input;
import nl.cwi.reo.runtime.Output;

public class WindowComponent {
	
	
	public static void produce(Output<Integer> A) {
		
	}
	
	public static void consume(Input<Integer> A) {
			A.get();
	}

}
