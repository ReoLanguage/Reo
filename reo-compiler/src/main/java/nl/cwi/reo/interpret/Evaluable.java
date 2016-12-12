package nl.cwi.reo.interpret;

import java.util.Map;

/**
 * A parameterized object definition. 
 */
public interface Evaluable<T> {
	
	/**
	 * Gets a generic instance of type T by instantiating parameters.
	 * @param p			parameter assignment
	 * @return generic instance of type T.
	 * @throws Exception if not all required parameters are assigned.
	 */
	public T evaluate(Map<String,Value> p) throws Exception;
}
