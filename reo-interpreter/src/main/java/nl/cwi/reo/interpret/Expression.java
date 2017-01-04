package nl.cwi.reo.interpret;

import java.util.Map;

public interface Expression extends Value {
	
	public Expression evaluate(Map<VariableName, Expression> params) throws Exception;

}
