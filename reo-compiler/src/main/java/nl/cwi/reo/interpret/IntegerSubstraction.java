package nl.cwi.reo.interpret;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IntegerSubstraction implements IntegerExpression {

	private IntegerExpression e1;
	
	private IntegerExpression e2;
	
	public IntegerSubstraction(IntegerExpression e1, IntegerExpression e2) {
		this.e1 = e1;
		this.e2 = e2;
	}

	@Override
	public Integer evaluate(Map<String, Value> p) throws Exception {
		return e1.evaluate(p) - e2.evaluate(p);
	}

	public List<String> variables() {
		List<String> vars = new ArrayList<String>(e1.variables());
		vars.addAll(e2.variables());
		return vars;
	}
}