package nl.cwi.reo.runtime.java.example;

import nl.cwi.reo.runtime.java.Input;
import nl.cwi.reo.runtime.java.Output;

public class MyTestComponents {
	
	public static void produce(Output<Integer> A) {
		long a = System.nanoTime();
		for (int i = 0; i < 3000; i++) {
			A.put(i);
		}
		long b = System.nanoTime();
		String time = String.format("%f", (b - a) / 1000000000.0);
		System.out.println("Producer finished after " + time + " sec.");
	}
	
	public static void consume(Input<Integer> A) {
		while (true) {
			System.out.println(A.get());
		}	
	}

}
