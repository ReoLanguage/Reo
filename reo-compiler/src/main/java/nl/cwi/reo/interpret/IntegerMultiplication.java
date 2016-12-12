package nl.cwi.reo.interpret;

import java.util.Map;

public class IntegerMultiplication implements IntegerExpression {

	private IntegerExpression e1;
	
	private IntegerExpression e2;
	
	public IntegerMultiplication(IntegerExpression e1, IntegerExpression e2) {
		this.e1 = e1;
		this.e2 = e2;
	}

	@Override
	public Integer evaluate(Map<String, Value> p) throws Exception {
		return e1.evaluate(p) * e2.evaluate(p);
	}
	
}
