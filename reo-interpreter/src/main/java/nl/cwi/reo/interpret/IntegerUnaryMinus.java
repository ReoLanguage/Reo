package nl.cwi.reo.interpret;

import java.util.Map;


public final class IntegerUnaryMinus implements IntegerExpression {

	private final IntegerExpression e;
	
	public IntegerUnaryMinus(IntegerExpression e) {
		if (e == null)
			throw new NullPointerException();
		this.e = e;
	}

	@Override
	public IntegerExpression evaluate(Map<VariableName, Expression> params) throws Exception {
		IntegerExpression x = e.evaluate(params);
		if (x instanceof IntegerValue)
			return IntegerValue.unarymin((IntegerValue)x);
		return new IntegerUnaryMinus(x);
	}
	
	@Override
	public String toString() {
		return "-(" + e + ")";
	}
}
