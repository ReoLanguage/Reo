package nl.cwi.reo.interpret;

import java.util.Map;

public interface ZDefinition extends ProgramExpression {
	
	public ZDefinition evaluate(Map<VariableName, Expression> params) throws Exception;
	
}
