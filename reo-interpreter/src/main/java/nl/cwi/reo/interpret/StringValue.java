package nl.cwi.reo.interpret;

import java.util.Map;


public class StringValue implements Expression {
	
	private final String str; 
	
	public StringValue(String str) {
		if (str == null)
			throw new NullPointerException();
		this.str = str;
	}
	
	public Expression evaluate(Map<VariableName, Expression> params) {
		return this;
	}
	
	@Override
	public String toString() {
		return str;
	}
}
