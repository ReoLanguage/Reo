package nl.cwi.reo.interpret;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IntegerRemainder implements IntegerExpression {

	private IntegerExpression e1;
	
	private IntegerExpression e2;
	
	public IntegerRemainder(IntegerExpression e1, IntegerExpression e2) {
		this.e1 = e1;
		this.e2 = e2;
	}

	@Override
	public Integer evaluate(Map<String, Value> p) throws Exception {
		Integer m = e2.evaluate(p);
		if (m == 0) throw new Exception("Cannot use zero as a modulus (a % 0).");
		return e1.evaluate(p) % m;
	}

	public List<String> variables() {
		List<String> vars = new ArrayList<String>(e1.variables());
		vars.addAll(e2.variables());
		return vars;
	}
	
}
