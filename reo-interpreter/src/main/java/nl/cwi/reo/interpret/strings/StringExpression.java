package nl.cwi.reo.interpret.strings;

import java.util.Map;

import nl.cwi.reo.errors.CompilationException;
import nl.cwi.reo.interpret.ranges.Expression;
import nl.cwi.reo.interpret.variables.VariableName;

public interface StringExpression extends Expression {

	public StringExpression evaluate(Map<VariableName, Expression> params) throws CompilationException;
	
}
