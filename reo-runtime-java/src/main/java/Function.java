import nl.cwi.reo.runtime.java.Input;
import nl.cwi.reo.runtime.java.Output;

public class Function {

	public static String add(String a, String b) {
//		if(a instanceof Integer && b instanceof Integer)
//			return (Integer)a+(Integer)b;
		return a+b;
	}
	
	public static void consumer(Input<String> a) {
		for (int i = 0; i < 300; i++)
			System.out.println(i + ": " + a.get() + " ");
		System.exit(0);
	}
}
