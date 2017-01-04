package nl.cwi.reo.interpret;

import java.util.Map;


public interface BooleanExpression extends Expression {

	public BooleanExpression evaluate(Map<VariableName, Expression> params) throws Exception;
	
}
