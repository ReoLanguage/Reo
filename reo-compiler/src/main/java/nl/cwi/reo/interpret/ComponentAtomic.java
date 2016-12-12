package nl.cwi.reo.interpret;

import java.util.Map;

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
	private final Signature params;

	/**
	 * Sorted set of typed node names.
	 */
	private final Signature intface;
	
	/**
	 * Constructor.
	 * @param atom			atomic component
	 * @param parameters	list of parameter names
	 * @param intface		list of node names 
	 */
	public ComponentAtomic(Atom atom, Signature params, Signature intface) {
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
		C.restrict(intface.evaluate(p).keySet());
		return C;
	}
	
	/**
	 * Gets the parameters of this atomic component definition
	 * @return Signature containing all parameters
	 */
	public Map<String, String> getParameters(Map<String, Value> p) 
			throws Exception {
		return this.params.evaluate(p);
	}

	/**
	 * Gets the interface of this atomic component definition
	 * @return Signature containing all nodes in the interface
	 */
	public Map<String, String> getInterface(Map<String, Value> p) 
			throws Exception {
		return this.intface.evaluate(p);
	}

}
