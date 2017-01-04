package nl.cwi.reo.interpret;

import java.util.Map;

public interface BodyDefinition extends BodyExpression {
	
	public BodyDefinition evaluate(Map<VariableName, Expression> params) throws Exception;
	
}
