package nl.cwi.reo.interpret;

import java.util.Map;

public class IntegerUnaryMinus implements IntegerExpression {

	private IntegerExpression e;
	
	public IntegerUnaryMinus(IntegerExpression e) {
		this.e = e;
	}

	@Override
	public Integer evaluate(Map<String, Value> p) throws Exception {
		return -e.evaluate(p);
	}

}
