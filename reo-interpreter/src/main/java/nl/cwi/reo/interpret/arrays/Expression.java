package nl.cwi.reo.interpret.arrays;

import java.util.Map;

import nl.cwi.reo.interpret.variables.VariableName;

public interface Expression extends Array {
	
	public Expression evaluate(Map<VariableName, Expression> params) throws Exception;

}
