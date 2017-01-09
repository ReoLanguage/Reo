package nl.cwi.reo.interpret;

import java.util.Map;


public final class BooleanNegation implements BooleanExpression {
	
	private final BooleanExpression e;
	
	public BooleanNegation(BooleanExpression e) {
		if (e == null)
			throw new NullPointerException();
		this.e = e;
	}
	
	public BooleanExpression evaluate(Map<VariableName, Expression> params) throws Exception {
		BooleanExpression x = e.evaluate(params);
		if (x instanceof BooleanValue)
			return BooleanValue.negation((BooleanValue)x);
		return new BooleanNegation(x);
	}
	
	@Override
	public String toString() {
		return "!(" + e + ")";
	}
}
