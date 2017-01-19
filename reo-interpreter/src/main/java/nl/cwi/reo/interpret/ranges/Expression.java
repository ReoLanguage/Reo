package nl.cwi.reo.interpret.ranges;

import java.util.Map;

import nl.cwi.reo.errors.CompilationException;
import nl.cwi.reo.interpret.variables.VariableName;

public interface Expression extends Range {
	
	public Expression evaluate(Map<VariableName, Expression> params) throws CompilationException;

}
