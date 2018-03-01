
public class Function {

	public static String concatenate(Object object) {
		if (object == null)
			throw new NullPointerException();

		String string = ((String) object) + " * ";

		return string;
	}
}
