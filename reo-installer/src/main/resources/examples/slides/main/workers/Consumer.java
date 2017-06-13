package workers;

import nl.cwi.reo.runtime.Input;
import nl.cwi.reo.runtime.Output;

public class Consumer {

        public static void blue(Input<String> port) {
                for (int i = 0; i < 10; i++) {
                        System.out.print(port.get());
                }
		System.exit(0);
        }
}
