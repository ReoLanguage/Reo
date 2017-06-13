package workers;

import nl.cwi.reo.runtime.Input;
import nl.cwi.reo.runtime.Output;

public class Producer {
	
        public static void red(Output<String> port) {
                for (int i = 1; i < 100; i++) {
                        port.put(i + ") Hello, ");
                }
		System.out.println("Red finished.");
        }

        public static void green(Output<String> port) {
                for (int i = 1; i < 100; i++) {
                        port.put("world!\n");
                }
		System.out.println("Green finished.");
        }
}
