package nl.cwi.reo.interpret;

import java.util.Map;


public interface IntegerExpression extends Expression {
	
	public IntegerExpression evaluate(Map<VariableName, Expression> params) throws Exception;
	
}
