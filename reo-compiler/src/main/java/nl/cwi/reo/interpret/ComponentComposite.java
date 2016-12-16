package nl.cwi.reo.interpret;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * A parameterized definition of a composition of a set {@link java.util.Set}&lt;{@link nl.cwi.reo.parse.Component}&gt; of parameterized components. 
 */
public class ComponentComposite implements ComponentExpression {
	
	private SignatureExpression sign;
	
	private ComponentBody body;
	
	/**
	 * Constructor.
	 * @param sign			signature
	 * @param components	set of subcomponents
	 */
	public ComponentComposite(SignatureExpression sign, ComponentBody body) {
		this.sign = sign;
		this.body = body;
	}
	
	/**
	 * Gets a concrete program from this parameterized component.
	 * @param p			parameter assignment
	 * @return concrete program {@link nl.cwi.reo.semantics.Program} for this parameterized component
	 * @throws Exception if not all required parameters are assigned.
	 */
	public Component evaluate(Map<String, Value> p) throws Exception {
		Component C = body.evaluate(p);		
		C.restrict(new HashSet<String>(sign.evaluate(p).getNodeNames()));
		return C;
	}

	public Map<String, String> getParameters(Map<String, Value> p)
			throws Exception {
		return null; //params.evaluate(p);
	}

	public Map<String, String> getInterface(Map<String, Value> p)
			throws Exception {
		return null; //intface.evaluate(p);
	}
	
	/**
	 * Gets all variables in order of occurrence. 
	 * @return list of all variables in order of occurrence.
	 */
	public List<String> variables() {
		List<String> vars = new ArrayList<String>(sign.variables());
		vars.addAll(body.variables());
		return vars;
	}
	
}
