package nl.cwi.reo.interpret.blocks;

import java.util.Map;

import nl.cwi.reo.interpret.ranges.Expression;
import nl.cwi.reo.interpret.variables.VariableName;
import nl.cwi.reo.semantics.Semantics;

public interface Statement<T extends Semantics<T>> {
	
	public Statement<T> evaluate(Map<VariableName, Expression> params) throws Exception;

}
