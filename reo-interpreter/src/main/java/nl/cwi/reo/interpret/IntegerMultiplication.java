package nl.cwi.reo.interpret;

import java.util.Map;


public final class IntegerMultiplication implements IntegerExpression {

	private final IntegerExpression e1;
	
	private final IntegerExpression e2;
	
	public IntegerMultiplication(IntegerExpression e1, IntegerExpression e2) {
		if (e1 == null || e2 == null)
			throw new NullPointerException();
		this.e1 = e1;
		this.e2 = e2;
	}

	@Override
	public IntegerExpression evaluate(Map<VariableName, Expression> params) throws Exception {
		IntegerExpression x1 = e1.evaluate(params);
		IntegerExpression x2 = e2.evaluate(params);
		if (x1 instanceof IntegerValue && x2 instanceof IntegerValue)
			return IntegerValue.multiplication((IntegerValue)x1, (IntegerValue)x2);
		return new IntegerMultiplication(x1, x2);
	}
	
	@Override
	public String toString() {
		return "(" + e1 + "*" + e2 + ")";
	}
}
