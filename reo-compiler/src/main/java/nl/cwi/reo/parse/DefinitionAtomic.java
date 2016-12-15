package nl.cwi.reo.parse;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.cwi.reo.semantics.Program;
import nl.cwi.reo.semantics.Instance;

/**
 * An atomic definition {@link nl.cwi.reo.parse.Definition} of a component defined in an abstract semantics of Reo.
 */
public class DefinitionAtomic implements Definition {
	
	/**
	 * Name of the atomic definition.
	 */
	private final String name;
	
	/**
	 * Parameters.
	 */
	private final List<String> parameters;
	
	/**
	 * Interface.
	 */
	private final List<String> intface;
	
	/**
	 * Set of input ports. All non-input ports are output ports
	 */
	private final Set<String> inputs;
	
	/**
	 * Atomic component
	 */
	private final Object atom;
	
	/**
	 * Constructor.
	 * @param parameters	list of parameter names
	 * @param intface		list of node names 
	 * @param atom			atomic component
	 */
	public DefinitionAtomic(String name, List<String> parameters, List<String> intface, Set<String> inputs, Object atom) {
		this.name = name;
		this.parameters = parameters;
		this.intface = intface;
		this.inputs = inputs;
		this.atom = atom;
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
	public List<String> getInterface(Map<String, String> parameters) 
			throws InstantiationException {
		return this.intface;
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
		Set<Instance> instances = new HashSet<Instance>();
		Map<String, String> input = new HashMap<String, String>();
		Map<String, String> output = new HashMap<String, String>();
		for (String port : this.intface) {
			if (this.inputs.contains(port))
				input.put(port, port);
			else
				output.put(port, port);		
		}
		instances.add(new Instance(this.name, this.atom, input, output));
		return new Program(instances);
	}

}
