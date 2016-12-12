package nl.cwi.reo.interpret;

import java.util.Map;

public class BooleanValue implements BooleanExpression {
	
	private boolean bool;
	
	public BooleanValue(boolean bool) {
		this.bool = bool;
	}

	@Override
	public Boolean evaluate(Map<String, Value> p) throws Exception {
		return bool;
	}

}
