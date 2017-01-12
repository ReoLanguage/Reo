package nl.cwi.reo.interpret.strings;

import java.util.Map;

import nl.cwi.reo.interpret.arrays.Expression;
import nl.cwi.reo.interpret.variables.VariableName;

public interface StringExpression extends Expression {

	public StringExpression evaluate(Map<VariableName, Expression> params) throws Exception;
	
}
