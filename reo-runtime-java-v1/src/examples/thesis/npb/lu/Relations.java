package nl.cwi.pr.runtime.examples.thesis.npb.lu;

public class Relations {

	public static boolean GreaterThan0(Object object) {
		return object instanceof Integer && ((Integer) object) > 0;
	}
}
