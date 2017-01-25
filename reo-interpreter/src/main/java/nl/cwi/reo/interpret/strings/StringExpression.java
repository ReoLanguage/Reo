package nl.cwi.reo.interpret.strings;

import java.util.Map;

import nl.cwi.reo.errors.CompilationException;
import nl.cwi.reo.interpret.ranges.Expression;

public interface StringExpression extends Expression {

	public StringExpression evaluate(Map<String, Expression> params) throws CompilationException;
	
}
