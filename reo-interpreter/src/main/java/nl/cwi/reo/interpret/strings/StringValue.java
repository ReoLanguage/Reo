package nl.cwi.reo.interpret.strings;

import java.util.Map;
import java.util.Objects;

import nl.cwi.reo.interpret.oldstuff.Expression;

public class StringValue implements StringExpression {
	
	private final String str; 
	
	public StringValue(String str) {
		if (str == null)
			throw new NullPointerException();
		this.str = str;
	}
	
	public StringValue evaluate(Map<String, Expression> params) {
		return this;
	}
	
	public static StringValue concatenate(StringValue v1, StringValue v2) {
		return new StringValue(v1.str + v2.str);
	}
	
	@Override
	public boolean equals(Object other) {
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof StringValue)) return false;
	    StringValue p = (StringValue)other;
	   	return Objects.equals(this.str, p.str);
	}
	
	@Override
	public int hashCode() {
	    return Objects.hash(this.str);
	}
	
	@Override
	public String toString() {
		return str;
	}
}
