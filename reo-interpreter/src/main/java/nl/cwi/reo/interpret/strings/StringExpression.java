package nl.cwi.reo.interpret.strings;

import java.util.Map;

import nl.cwi.reo.interpret.expressions.ValueExpression;
import nl.cwi.reo.semantics.api.Expression;

public interface StringExpression extends ValueExpression {

	public StringExpression evaluate(Map<String, Expression> params);
	
}
