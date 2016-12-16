package nl.cwi.reo.interpret;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * An atomic definition {@link nl.cwi.reo.parse.Definition} of a component defined in an abstract semantics of Reo.
 */
public class ComponentAtomic implements ComponentExpression {

	/**
	 * Signature.
	 */
	private final SignatureExpression sign;
	
	/**
	 * Atomic component
	 */
	private final AtomExpression atom;
	
	/**
	 * Constructor.
	 * @param atom			atomic component
	 * @param parameters	list of parameter names
	 * @param intface		list of node names 
	 */
	public ComponentAtomic(SignatureExpression sign, AtomExpression atom) {
		this.atom = atom;
		this.sign = sign;
	}	

	/**
	 * Gets a concrete program from this parameterized component.
	 * @param p		parameter assignment
	 * @return concrete program {@link nl.cwi.reo.semantics.Program} for this parameterized component
	 * @throws Exception if not all required parameters are assigned.
	 */
	public Component evaluate(Map<String, Value> p) throws Exception {	
		Component C = new Component(atom.evaluate(p));		
		C.restrict(new HashSet<String>(sign.evaluate(p).getNodeNames()));
		return C;
	}
	
	/**
	 * Gets all variables in order of occurrence. 
	 * @return list of all variables in order of occurrence.
	 */
	public List<String> variables() {
		List<String> vars = new ArrayList<String>(sign.variables());
		vars.addAll(atom.variables());
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
