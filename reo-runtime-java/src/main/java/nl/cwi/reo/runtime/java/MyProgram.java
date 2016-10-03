package nl.cwi.reo.runtime.java;

public class MyProgram {
	
	public static void produce(Output<Integer> A) {
		long a = System.nanoTime();
		for (int i = 0; i < 3000000; i++) {
			A.put(i);
		}
		long b = System.nanoTime();
		String time = String.format("%f", (b - a) / 1000000000.0);
		System.out.println("Producer finished after " + time + " sec.");
	}
	
	public static void consume(Input<Integer> A) {
		while (true) {
			A.get();
		}	
	}

}
