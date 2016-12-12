package nl.cwi.reo.interpret;

import java.util.Map;

public class BooleanNegation implements BooleanExpression {
	
	private BooleanExpression e;
	
	public BooleanNegation(BooleanExpression e) {
		this.e = e;
	}
	
	public Boolean evaluate(Map<String, Value> p) throws Exception {
		return !e.evaluate(p);
	}
}
