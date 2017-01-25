package nl.cwi.reo.interpret.booleans;

import java.util.Map;

import nl.cwi.reo.errors.CompilationException;
import nl.cwi.reo.interpret.ranges.Expression;


public interface BooleanExpression extends Expression {

	public BooleanExpression evaluate(Map<String, Expression> params) throws CompilationException;
	
}
