
import nl.cwi.reo.runtime.Input;
import nl.cwi.reo.runtime.Output;

public class Producer {
	
        public static void red(Output<String> a) {
                for (int i = 1; i < 100; i++) {
                        a.put(i + ") Hello, ");
                }
		System.out.println("Red finished.");
        }

        public static void green(Output<String> a) {
                for (int i = 1; i < 100; i++) {
                        a.put("world!\n");
                }
		System.out.println("Green finished.");
        }
}
