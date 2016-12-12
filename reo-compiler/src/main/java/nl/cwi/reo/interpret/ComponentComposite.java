package nl.cwi.reo.interpret;

import java.util.Map;

/**
 * A parameterized definition of a composition of a set {@link java.util.Set}&lt;{@link nl.cwi.reo.parse.Component}&gt; of parameterized components. 
 */
public class ComponentComposite implements ComponentExpression {
	
	private Signature params;
	
	private Signature intface;
	
	private ComponentBody body;
	
	/**
	 * Constructor.
	 * @param parameters	list of parameter names
	 * @param intface		interface of the definition 
	 * @param components	set of subcomponents
	 */
	public ComponentComposite(ComponentBody body, Signature params, Signature intface) {
		this.intface = intface;
		this.params = params;
		this.body = body;
	}
	
	/**
	 * Gets a concrete program from this parameterized component.
	 * @param p			parameter assignment
	 * @return concrete program {@link nl.cwi.reo.semantics.Program} for this parameterized component
	 * @throws Exception if not all required parameters are assigned.
	 */
	public Component evaluate(Map<String, Value> p) 
			throws Exception {		
		return body.evaluate(p);
	}

	public Map<String, String> getParameters(Map<String, Value> p)
			throws Exception {
		return params.evaluate(p);
	}

	public Map<String, String> getInterface(Map<String, Value> p)
			throws Exception {
		return intface.evaluate(p);
	}

}
