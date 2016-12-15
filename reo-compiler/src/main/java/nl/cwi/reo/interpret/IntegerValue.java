package nl.cwi.reo.interpret;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IntegerValue implements IntegerExpression {
	
	/**
	 * Natural number.
	 */
	private int n;
	
	/**
	 * Constructs a natural number from a string.
	 * @param s 	string representation of a natural number
	 */
	public IntegerValue(String s) {
		this.n = Integer.valueOf(s);
	}

	/**
	 * Evaluates this natural number to a Integer.
	 * @param p		 	parameter assignment
	 * @return Integer evaluation with respect to parameter assignment.
	 */
	public Integer evaluate(Map<String, Value> p) throws Exception {
		return this.n;
	}

	public List<String> variables() {
		return new ArrayList<String>();
	}	
	
}
