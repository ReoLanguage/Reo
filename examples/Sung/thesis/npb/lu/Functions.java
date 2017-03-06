package nl.cwi.pr.runtime.examples.thesis.npb.lu;

public class Functions {

	public static int decrement(Object object) {
		if (object == null)
			throw new NullPointerException();
		if (!(object instanceof Integer))
			throw new IllegalArgumentException();

		return ((Integer) object) - 1;
	}

	public static int increment(Object object) {
		if (object == null)
			throw new NullPointerException();
		if (!(object instanceof Integer))
			throw new IllegalArgumentException();

		return ((Integer) object) + 1;
	}
}
