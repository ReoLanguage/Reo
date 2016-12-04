package nl.cwi.reo.seepageautomata;

import java.util.HashMap;
import java.util.Map;

import nl.cwi.reo.seepageautomata.PositiveBooleanConstraint;

public class SeepageFunction {

	/**
	 * Set of boolean conditions for each port.
	 */
	private final Map<String, PositiveBooleanConstraint> B;
	
	public SeepageFunction() {
		B = new HashMap<String, PositiveBooleanConstraint>();
	}
}
