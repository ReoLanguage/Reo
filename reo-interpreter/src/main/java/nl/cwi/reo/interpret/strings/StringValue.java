package nl.cwi.reo.interpret.strings;

import java.util.Map;

import nl.cwi.reo.interpret.arrays.Expression;
import nl.cwi.reo.interpret.variables.VariableName;


public class StringValue implements StringExpression {
	
	private final String str; 
	
	public StringValue(String str) {
		if (str == null)
			throw new NullPointerException();
		this.str = str;
	}
	
	public StringValue evaluate(Map<VariableName, Expression> params) {
		return this;
	}
	
	public static StringValue concatenate(StringValue v1, StringValue v2) {
		return new StringValue(v1.str + v2.str);
	}
	
	@Override
	public String toString() {
		return str;
	}
}
