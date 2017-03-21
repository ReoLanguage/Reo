package workers;

import nl.cwi.pr.runtime.api.InputPort;
import nl.cwi.pr.runtime.api.OutputPort;

public class Consumer {

        public static void cons(InputPort port) {
                for (int i = 0; i < 1000; i++) {
                        System.out.println(port.getUninterruptibly());
                }
        }
}
