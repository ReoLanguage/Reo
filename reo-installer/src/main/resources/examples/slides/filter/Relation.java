
public class Relation {

	public static boolean Even(String datum) {
		if (datum == null) return false;
		return Integer.parseInt(datum) % 2 == 0;
	}

	public static boolean Odd(String datum) {
		if (datum == null) return false;
		return Integer.parseInt(datum) % 2 != 0;
	}

}
