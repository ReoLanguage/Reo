package nl.cwi.pr.runtime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Datum {

	/**
	 * Checks if the string <code>string</code> can be converted to an object.
	 * 
	 * @return <code>true</code> if <code>string</code> can be converted to an
	 *         object; <code>false</code> otherwise.
	 */
	public static boolean canConvertToObject(String string) {
		if (string == null)
			throw new NullPointerException();

		try {
			convertToObject(string);
			return true;
		} catch (final IllegalArgumentException exception) {
			return false;
		}
	}

	/**
	 * Converts the string <code>string</code> to an object.
	 * 
	 * @param string
	 *            The string. Possibly <code>null</code>.
	 * @return An object.
	 * @throws IllegalArgumentException
	 *             If <code>!canConvertToObject()</code>.
	 */
	public static Serializable convertToObject(String string) {
		if (string == null)
			throw new NullPointerException();

		if (string.equals("null"))
			return null;

		String[] tokens = string.split(" ");
		List<Serializable> list = new ArrayList<Serializable>();

		int nTokens = tokens.length;
		boolean isArray = tokens[0].startsWith("[")
				&& tokens[tokens.length - 1].endsWith("]");

		if (isArray) {
			tokens[0] = tokens[0].substring(1);
			tokens[nTokens - 1] = tokens[nTokens - 1].substring(0,
					tokens[nTokens - 1].length() - 1);
		}

		StringBuilder builder = new StringBuilder();
		for (String str : tokens) {
			if (str.isEmpty())
				continue;

			/*
			 * Start building a string if str starts with a double quote.
			 */

			if (builder.length() == 0 && str.startsWith("\""))
				if (str.endsWith("\""))
					list.add(str.substring(1, str.length() - 1));
				else
					builder.append(str.substring(1));

			/*
			 * Parse str and add it to the list otherwise.
			 */

			else if (builder.length() == 0 && !str.startsWith("\"")) {

				/*
				 * Try to parse str as an integer
				 */

				try {
					list.add(Integer.parseInt(str));
					if (isArray)
						continue;
					else
						break;
				} catch (NumberFormatException exception) {
				}

				/*
				 * Try to parse str as a float
				 */

				try {
					list.add(Float.parseFloat(str));
					if (isArray)
						continue;
					else
						break;
				} catch (final NumberFormatException exception) {
				}

				/*
				 * Try to parse str as a boolean
				 */

				if (str.length() == 4 && str.toLowerCase().equals("true")) {
					list.add(true);
					if (isArray)
						continue;
					else
						break;
				}

				if (str.length() == 5 && str.toLowerCase().equals("false")) {
					list.add(false);
					if (isArray)
						continue;
					else
						break;
				}

				/*
				 * Try to parse str as null
				 */

				if (str.length() == 4 && str.equals("null")) {
					list.add(null);
					if (isArray)
						continue;
					else
						break;
				}

				throw new IllegalArgumentException();
			}

			/*
			 * Stop building a string if str ends with a double quote
			 */

			else if (str.endsWith("\"")) {
				builder.append(" " + str.substring(0, str.length() - 1));
				list.add(builder.toString());
				builder.setLength(0);
			}

			/*
			 * Continue building a string
			 */

			else
				builder.append(" " + str);
		}

		if (builder.length() > 0)
			throw new IllegalArgumentException();

		if (!list.isEmpty() && !isArray)
			return list.get(0);
		else
			return list.toArray();
	}

	/**
	 * Converts the object <code>object</code> to a string.
	 * 
	 * @param object
	 *            The object. Not <code>null</code>.
	 * @return A string. Never <code>null</code>.
	 * @throws NullPointerException
	 *             If <code>item==null</code>.
	 */
	public static String convertToString(Object object) {
		if (object == null)
			return "null";

		if (object instanceof Object[]) {
			StringBuilder builder = new StringBuilder();
			builder.append("[");
			for (Object o : ((Object[]) object))
				builder.append(convertToString(o)).append(", ");

			if (builder.length() > 1)
				builder.setLength(builder.length() - 2);

			builder.append("]");
			return builder.toString();
		} else {

			String className = object.getClass().getSimpleName();

			return object == null ? "null" : (object instanceof String ? "\""
					: "")
					+ object.toString()
					+ (object instanceof String ? "\"" : "")
					+ (className.isEmpty() ? " (anonymous type)" : ":"
							+ className);
		}
	}

}
