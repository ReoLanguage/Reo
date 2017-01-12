package nl.cwi.reo.interpret.programs;

import java.util.Map;

import nl.cwi.reo.interpret.arrays.Expression;
import nl.cwi.reo.interpret.variables.VariableName;

public interface ProgramExpression {
	
	public ProgramExpression evaluate(Map<VariableName, Expression> params) throws Exception;

}
