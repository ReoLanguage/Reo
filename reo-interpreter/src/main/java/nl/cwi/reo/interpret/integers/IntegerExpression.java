package nl.cwi.reo.interpret.integers;

import java.util.Map;

import nl.cwi.reo.interpret.ranges.Expression;
import nl.cwi.reo.interpret.variables.VariableName;


public interface IntegerExpression extends Expression {
	
	public IntegerExpression evaluate(Map<VariableName, Expression> params) throws Exception;
	
}
