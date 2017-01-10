package nl.cwi.reo.interpret;

import java.util.Map;

public interface StringExpression extends Expression {

	public StringExpression evaluate(Map<VariableName, Expression> params) throws Exception;
	
}
