package nl.cwi.reo.interpret;

import java.util.List;
import java.util.Map;

public class ParameterListExpression implements Expression<ParameterList> {
	
	private List<ParameterExpression> params;

	@Override
	public ParameterList evaluate(Map<String, Value> p) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> variables() {
		// TODO Auto-generated method stub
		return null;
	}

}
