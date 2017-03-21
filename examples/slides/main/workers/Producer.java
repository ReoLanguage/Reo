package workers;

import nl.cwi.pr.runtime.api.InputPort;
import nl.cwi.pr.runtime.api.OutputPort;

public class Producer {
	
        public static void prod(OutputPort port) {
                for (int i = 0; i < 1000; i++) {
                        port.putUninterruptibly("data" + i);
                }
        }
}
