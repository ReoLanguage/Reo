import nl.cwi.reo.runtime.Input;
import nl.cwi.reo.runtime.Output;

public class Consumer {

        public static void blue(Input<String> port) {
                while(true) {
                        System.out.print(port.get());
                }
        }
}
