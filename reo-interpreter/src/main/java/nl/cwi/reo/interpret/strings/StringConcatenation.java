package nl.cwi.reo.interpret.strings;

import java.util.Map;

import nl.cwi.reo.interpret.arrays.Expression;
import nl.cwi.reo.interpret.variables.VariableName;

public final class StringConcatenation implements StringExpression {
	
	private final StringExpression e1;
	private final StringExpression e2;
	
	public StringConcatenation(StringExpression e1, StringExpression e2) {
		if (e1 == null || e2 == null)
			throw new NullPointerException();
		this.e1 = e1;
		this.e2 = e2;
	}
	
	public StringExpression evaluate(Map<VariableName, Expression> params) throws Exception {
		StringExpression x1 = e1.evaluate(params);
		StringExpression x2 = e2.evaluate(params);
		if (x1 instanceof StringValue && x2 instanceof StringValue)
			return StringValue.concatenate((StringValue)x1, (StringValue)x2);
		return new StringConcatenation(x1, x2);
	}
	
	@Override
	public String toString() {
		return e1 + "+" + e2;
	}
}