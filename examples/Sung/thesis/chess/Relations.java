package nl.cwi.pr.runtime.examples.thesis.chess;

public class Relations {

	public static boolean Move(Object object) {
		if (object == null)
			throw new NullPointerException();
		if (!(object instanceof String))
			throw new IllegalArgumentException();

		return !"(none)".equals(object);
	}
}
