package nl.cwi.reo.interpret;

import java.util.Map;

public interface ComponentExpression extends Expression<Component> {

	public Map<String, String> getParameters(Map<String, Value> p)
			throws Exception;
	
	public Map<String, String> getInterface(Map<String, Value> p)
			throws Exception;

}
