//package unfixedSwitch;

public class FilterFunction {

	public static boolean ControllerMessage(String data) {
		// boolean a = false;
		if (data == null)
			throw new NullPointerException();

		else {
			String[] msarr = data.toString().split("/");

			if (msarr[1].contains("RULE"))

				return true;
			else
				return false;

		}

	}

	public static boolean RegularMessage(String data) {
		if (data == null)
			throw new NullPointerException();

		else {
			String[] msarr = data.toString().split("/");

			if (msarr[1].contains("MSG"))

				return true;
			else
				return false;
		}
	}

	public static boolean Sendout_q0(String data) {
		String str[] = data.split("/");
		String str5 = str[5];
		
		if (str5.contains("q0")) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean Sendout_q1(String data) {
		String str[] = data.split("/");
		String str5 = str[5];

		
		if (str5.contains("q1")) {
			return true;
		} else {
			return false;
		}

	}

	public static boolean Sendout_q2(String data) {
		String str[] = data.split("/");
		String str5 = str[5];


		if (str5.contains("q2")) {
			return true;
		} else {
			return false;
		}

	}

}
