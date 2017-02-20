package nl.cwi.reo.interpret.booleans;

import java.util.Map;

import nl.cwi.reo.interpret.expressions.ValueExpression;
import nl.cwi.reo.interpret.oldstuff.Expression;

public interface BooleanExpression extends ValueExpression {

	public BooleanExpression evaluate(Map<String, Expression> params);
	
}
