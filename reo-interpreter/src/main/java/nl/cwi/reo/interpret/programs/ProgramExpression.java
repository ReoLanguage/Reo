package nl.cwi.reo.interpret.programs;

import java.util.Map;

import nl.cwi.reo.interpret.ranges.Expression;
import nl.cwi.reo.interpret.variables.VariableName;
import nl.cwi.reo.semantics.Semantics;

public interface ProgramExpression<T extends Semantics<T>> {
	
	public ProgramExpression<T> evaluate(Map<VariableName, Expression> params) throws Exception;

}
