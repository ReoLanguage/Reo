package nl.cwi.pr.runtime.examples.thesis.basic;

public class Functions {

	public static Integer add(Object left, Object right) {
		if (left == null)
			throw new NullPointerException();
		if (right == null)
			throw new NullPointerException();
		if (!(left instanceof Integer))
			throw new IllegalArgumentException();
		if (!(right instanceof Integer))
			throw new IllegalArgumentException();

		return ((Integer) left) + ((Integer) right);
	}
}
