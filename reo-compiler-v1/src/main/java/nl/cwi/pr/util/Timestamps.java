package nl.cwi.pr.util;

import java.text.SimpleDateFormat;

public class Timestamps {

	//
	// STATIC - METHODS - PUBLIC
	//

	public static String getNext() {
		try {
			Thread.sleep(1);
		} catch (InterruptedException exception) {
		}

		long millis = System.currentTimeMillis();
		return "d" + new SimpleDateFormat("yyyyMMdd").format(millis) + "_t"
				+ new SimpleDateFormat("HHmmss").format(millis)
				+ "_" + new SimpleDateFormat("SSS").format(millis);
	}
}
