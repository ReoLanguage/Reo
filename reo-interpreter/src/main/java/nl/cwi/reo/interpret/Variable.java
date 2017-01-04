package nl.cwi.reo.interpret;

import java.util.Map;

public interface Variable extends Expression {
	
	public Variable evaluate(Map<VariableName, Expression> params) throws Exception;

}
