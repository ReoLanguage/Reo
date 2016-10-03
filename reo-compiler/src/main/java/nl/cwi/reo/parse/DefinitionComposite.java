package nl.cwi.reo.parse;

import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.cwi.reo.semantics.Program;

/**
 * A parameterized definition of a composition of a set {@link java.util.Set}&lt;{@link nl.cwi.reo.parse.Component}&gt; of parameterized components. 
 */
public class DefinitionComposite implements Definition {
	
	/**
	 * Parameters.
	 */
	private final List<String> parameters;
	
	/**
	 * Interface.
	 */
	private final Interface intface;
	
	/**
	 * Components.
	 */
	private final Set<Component> components;
	
	/**
	 * Constructor.
	 * @param parameters	list of parameter names
	 * @param intface		interface of the definition 
	 * @param components	set of subcomponents
	 */
	public DefinitionComposite(List<String> parameters, Interface intface, Set<Component> components) {
		this.parameters = parameters;
		this.intface = intface;
		this.components = components;
	}
	
	/**
	 * Gets the parameters.
	 * @return list of parameter names
	 */
	@Override
	public List<String> getParameters() {
		return this.parameters;
	}
	
	/**
	 * Gets the concrete component interface.
	 * @param parameters	parameter assignment
	 * @return list of node names
	 * @throws Exception if not all required parameters are assigned.
	 */
	@Override
	public List<String> getInterface(Map<String, String> parameters) throws Exception{
		return this.intface.getInterface(parameters);
	}
	
	/**
	 * Gets a concrete program from this parameterized component.
	 * @param parameters		parameter assignment
	 * @param generator			fresh node name generator
	 * @return concrete program {@link nl.cwi.reo.semantics.Program} for this parameterized component
	 * @throws Exception if not all required parameters are assigned.
	 */
	@Override
	public Program getProgram(Map<String, String> parameters, NodeGenerator gen) throws Exception {
		
		// Initialize a new Reo program.
		Program program = new Program();
		
		// Gets all subprograms via a recursive call.
		// Note that the generator makes sure that each
		// recursive call uses new fresh node names.
		for (Component comp : this.components) 
			program.add(comp.getProgram(parameters, gen));
		
		return program;
	}

}
