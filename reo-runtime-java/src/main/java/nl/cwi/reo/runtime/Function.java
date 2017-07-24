<<<<<<< HEAD:reo-runtime-java/src/main/java/nl/cwi/reo/runtime/Function.java
package nl.cwi.reo.runtime;
=======
package nl.cwi.reo.components;

>>>>>>> e50a35dacf71adbb00dc785733842572571ebe7a:reo-runtime-java/src/main/java/nl/cwi/reo/components/Function.java
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

<<<<<<< HEAD:reo-runtime-java/src/main/java/nl/cwi/reo/runtime/Function.java
=======
import nl.cwi.reo.runtime.Input;

>>>>>>> e50a35dacf71adbb00dc785733842572571ebe7a:reo-runtime-java/src/main/java/nl/cwi/reo/components/Function.java
public class Function {

	public static String add(String a, String b) {
		// if(a instanceof Integer && b instanceof Integer)
		// return (Integer)a+(Integer)b;
		return a + b;
	}

	public static String majority(Object object) {
		if (object == null)
			throw new NullPointerException();

		String[] candidateMoves = ((String) object).split(" ");
		Map<String, AtomicInteger> table = new HashMap<>();

		for (String str : candidateMoves)
			if (!str.equals("nomove")) {
				if (!table.containsKey(str))
					table.put(str, new AtomicInteger(0));

				table.get(str).incrementAndGet();
			}

		String result = "nomove";
		int maxVotes = 0;
		for (Entry<String, AtomicInteger> entr : table.entrySet())
			if (entr.getValue().get() > maxVotes) {
				maxVotes = entr.getValue().get();
				result = entr.getKey();
			}

		return result;
	}

	public static void consumer(Input<String> a) {
		for (int i = 0; i < 300; i++)
			System.out.println(i + ": " + a.get() + " ");
		System.exit(0);
	}

	public static String concatenate(Object object1, Object object2) {
		if (object1 == null)
			throw new NullPointerException();
		if (object2 == null)
			throw new NullPointerException();
		if (!(object1 instanceof String))
			throw new IllegalArgumentException();
		if (!(object2 instanceof String))
			throw new IllegalArgumentException();

		String string = (((String) object1) + " " + ((String) object2)).trim();
		if (string.length() > 10000)
			string = "";

		return string;
	}

	public static String parse(Object object) {
		if (object == null)
			throw new NullPointerException();

		String result = "nomove";
		for (String str : ((String) object).split("\\n"))
			if (str.startsWith("bestmove")) {
				String[] tokens = str.split(" ");
				if (tokens.length > 1 && !result.startsWith("("))
					result = tokens[1];
			}

		return result;
	}
}
