package nl.cwi.reo.templates;

public class Relations {

	public static boolean Even(Object datum) {
		return datum instanceof Integer ? ((Integer) datum) % 2 == 0 : false;
	}

	public static boolean Odd(Object datum) {
		return datum instanceof Integer ? ((Integer) datum) % 2 == 1 : false;
	}

	public static boolean True(Object... data) {
		return true;
	}
	public static boolean Move(Object object) {
		if (object == null)
			throw new NullPointerException();
		if (!(object instanceof String))
			throw new IllegalArgumentException();

		return !"(none)".equals(object);
	}
}
