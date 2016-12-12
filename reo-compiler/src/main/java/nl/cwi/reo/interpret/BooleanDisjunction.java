package nl.cwi.reo.interpret;

import java.util.Map;

public class BooleanDisjunction implements BooleanExpression {
	
	private BooleanExpression e1;
	private BooleanExpression e2;
	
	public BooleanDisjunction(BooleanExpression e1, BooleanExpression e2) {
		this.e1 = e1;
		this.e2 = e2;
	}
	
	public Boolean evaluate(Map<String, Value> p) throws Exception {
		return e1.evaluate(p) || e2.evaluate(p);
	}
}