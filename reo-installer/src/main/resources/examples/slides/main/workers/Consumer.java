package workers;

import nl.cwi.pr.runtime.api.InputPort;
import nl.cwi.pr.runtime.api.OutputPort;

public class Consumer {

        public static void blue(InputPort port) {
                for (int i = 0; i < 10; i++) {
                        System.out.print(port.getUninterruptibly());
                }
        }
}
