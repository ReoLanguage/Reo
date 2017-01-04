package nl.cwi.reo.interpret;

import java.util.Map;


public final class BooleanLessThan implements BooleanExpression {

	private final IntegerExpression e1;
	
	private final IntegerExpression e2;
	
	public BooleanLessThan(IntegerExpression e1, IntegerExpression e2) {
		if (e1 == null || e2 == null)
			throw new IllegalArgumentException("Arguments cannot be null.");
		this.e1 = e1;
		this.e2 = e2;
	}

	@Override
	public BooleanExpression evaluate(Map<VariableName, Expression> params) throws Exception {
		IntegerExpression x1 = e1.evaluate(params);
		IntegerExpression x2 = e2.evaluate(params);
		if (x1 instanceof IntegerValue && x2 instanceof IntegerValue)
			return IntegerValue.lt((IntegerValue)x1, (IntegerValue)x2);
		return new BooleanEquality(x1, x2);
	}
	
	@Override
	public String toString() {
		return "(" + e1 + "<" + e2 + ")";
	}
}
