
public class Relation {

	public static boolean Even(Object datum) {
		return datum instanceof Integer ? ((Integer) datum) % 2 == 0 : false;
	}

	public static boolean Odd(Object datum) {
		return datum instanceof Integer ? ((Integer) datum) % 2 == 1 : false;
	}

}
