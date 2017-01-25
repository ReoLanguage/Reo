package nl.cwi.pr.runtime.examples.thesis.basic;

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
}
