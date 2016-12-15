package nl.cwi.reo.interpret;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BooleanConjunction implements BooleanExpression {
	
	private BooleanExpression e1;
	private BooleanExpression e2;
	
	public BooleanConjunction(BooleanExpression e1, BooleanExpression e2) {
		this.e1 = e1;
		this.e2 = e2;
	}
	
	public Boolean evaluate(Map<String, Value> p) throws Exception {
		return e1.evaluate(p) && e2.evaluate(p);
	}

	public List<String> variables() {
		List<String> vars = new ArrayList<String>(e1.variables());
		vars.addAll(e2.variables());
		return vars;
	}
}