package nl.cwi.reo.interpret;

import java.util.Map;

public interface ProgramExpression {
	
	public ProgramExpression evaluate(Map<VariableName, Expression> params) throws Exception;

}
