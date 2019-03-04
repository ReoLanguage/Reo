package nl.cwi.reo.runtime;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import nl.cwi.reo.runtime.Input;

public class WSemiring {

	public static int join(int a, int b) {
		return a + b;
	}
	
	public static boolean lowerEq(int a,int b) {
		return a<=b;
	}
}
