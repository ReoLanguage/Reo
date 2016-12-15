package nl.cwi.reo.interpret;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParameterExpression implements Expression<Parameter> {
	
	private Variables var;
	
	private ParameterType type;
	
	public ParameterExpression(Variables var, ParameterType type) {
		this.var = var;
		this.type = type;
	}

	public Parameter evaluate(Map<String, Value> p) throws Exception {
		
		List<String> v = var.evaluate(p);
		
		// TODO Auto-generated method stub
		return null;
	}

	public List<String> variables() {
		List<String> vars = new ArrayList<String>();
		vars.addAll(var.variables());
		return null;
	}

}
