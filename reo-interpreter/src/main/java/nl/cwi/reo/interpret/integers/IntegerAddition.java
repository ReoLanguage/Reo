package nl.cwi.reo.interpret.integers;

import java.util.Map;

import nl.cwi.reo.errors.CompilationException;
import nl.cwi.reo.interpret.ranges.Expression;
import nl.cwi.reo.interpret.variables.VariableName;


public final class IntegerAddition implements IntegerExpression {

	private final IntegerExpression e1;
	
	private final IntegerExpression e2;
	
	public IntegerAddition(IntegerExpression e1, IntegerExpression e2) {
		if (e1 == null || e2 == null)
			throw new NullPointerException();
		this.e1 = e1;
		this.e2 = e2;
	}

	@Override
	public IntegerExpression evaluate(Map<VariableName, Expression> params) throws CompilationException {
		IntegerExpression x1 = e1.evaluate(params);
		IntegerExpression x2 = e2.evaluate(params);
		if (x1 instanceof IntegerValue && x2 instanceof IntegerValue)
			return IntegerValue.addition((IntegerValue)x1, (IntegerValue)x2);
		return new IntegerAddition(x1, x2);
	}
	
	@Override
	public String toString() {
		return "(" + e1 + "+" + e2 + ")";
	}
}
