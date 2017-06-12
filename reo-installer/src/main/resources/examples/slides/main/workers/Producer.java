package workers;

import nl.cwi.pr.runtime.api.InputPort;
import nl.cwi.pr.runtime.api.OutputPort;

public class Producer {
	
        public static void red(OutputPort port) {
                for (int i = 1; i < 100; i++) {
                        port.putUninterruptibly(i + ") Hello, ");
                }
		System.out.println("Red finished.");
        }

        public static void green(OutputPort port) {
                for (int i = 1; i < 100; i++) {
                        port.putUninterruptibly("world!\n");
                }
		System.out.println("Green finished.");
        }
}
