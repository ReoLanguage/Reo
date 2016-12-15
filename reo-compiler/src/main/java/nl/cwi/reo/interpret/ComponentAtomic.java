package nl.cwi.reo.interpret;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An atomic definition {@link nl.cwi.reo.parse.Definition} of a component defined in an abstract semantics of Reo.
 */
public class ComponentAtomic implements ComponentExpression {
	
	/**
	 * Atomic component
	 */
	private final Atom atom;

	/**
	 * Sorted set of typed parameter names.
	 */
	private final ParameterListExpression params;

	/**
	 * Sorted set of typed node names.
	 */
	private final ParameterListExpression intface;
	
	/**
	 * Constructor.
	 * @param atom			atomic component
	 * @param parameters	list of parameter names
	 * @param intface		list of node names 
	 */
	public ComponentAtomic(Atom atom, ParameterListExpression params, ParameterListExpression intface) {
		this.atom = atom;
		this.params = params;
		this.intface = intface;
	}	

	/**
	 * Gets a concrete program from this parameterized component.
	 * @param p		parameter assignment
	 * @return concrete program {@link nl.cwi.reo.semantics.Program} for this parameterized component
	 * @throws Exception if not all required parameters are assigned.
	 */
	public Component evaluate(Map<String, Value> p) 
			throws Exception {	
		Component C = new Component(atom);
		C.restrict((Set)intface.evaluate(p).params);
		return C;
	}
	
	/**
	 * Gets all variables in order of occurrence. 
	 * @return list of all variables in order of occurrence.
	 */
	public List<String> variables() {
		List<String> vars = new ArrayList<String>(params.variables());
		vars.addAll(intface.variables());
		return vars;
	}
	
	/**
	 * Gets the parameters of this atomic component definition
	 * @return Signature containing all parameters
	 */
	public Map<String, String> getParameters(Map<String, Value> p) 
			throws Exception {
		return null;// this.params.evaluate(p);
	}

	/**
	 * Gets the interface of this atomic component definition
	 * @return Signature containing all nodes in the interface
	 */
	public Map<String, String> getInterface(Map<String, Value> p) 
			throws Exception {
		return null;// this.intface.evaluate(p);
	}

}
