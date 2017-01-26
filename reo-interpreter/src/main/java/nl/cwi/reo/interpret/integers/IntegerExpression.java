package nl.cwi.reo.interpret.integers;

import java.util.Map;

import nl.cwi.reo.interpret.expressions.ValueExpression;
import nl.cwi.reo.semantics.api.Expression;

public interface IntegerExpression extends ValueExpression {
	
	public IntegerExpression evaluate(Map<String, Expression> params);
	
}
