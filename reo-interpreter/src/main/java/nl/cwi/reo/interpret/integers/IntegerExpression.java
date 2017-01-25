package nl.cwi.reo.interpret.integers;

import java.util.Map;

import nl.cwi.reo.errors.CompilationException;
import nl.cwi.reo.interpret.ranges.Expression;


public interface IntegerExpression extends Expression {
	
	public IntegerExpression evaluate(Map<String, Expression> params) throws CompilationException;
	
}
