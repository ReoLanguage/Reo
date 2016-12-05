package nl.cwi.reo.Treoparse;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.cwi.reo.semantics.Instance;
import nl.cwi.reo.semantics.Program;

/**
 * A parameterized reference to component definition {@link nl.cwi.reo.parse.Definition}.
 */
public final class InstanceReference implements Instance {
	
	/**
	 * Parameter assignment.
	 */
	public final List<String> parameters;
	
	/**
	 * Abstract interface of the component reference
	 */
	private final Interface intface;

	/**
	 * Abstract component definition.
	 */
	public Component definition;
	
	/**
	 * Constructs an abstract component that instantiate a defined abstract component. 
	 * @param parameters		parameters of the referring component
	 * @param intface			interface of the referring component
	 * @param definition		defining component
	 */
	public InstanceReference(List<String> parameters, Interface intface, Component definition) {
		this.parameters = parameters;
		this.intface = intface;
		this.definition = definition;
	}
	
	/**
	 * Gets a concrete program from this parameterized component.
	 * @param parameters		parameter assignment
	 * @param generator			fresh node name generator
	 * @return concrete program {@link nl.cwi.reo.semantics.Program} for this parameterized component
	 * @throws Exception if not all required parameters are assigned.
	 */
	@Override
	public Program getProgram(Map<String, String> parameters, NodeGenerator generator) throws Exception {
		
		// Find the assignment of parameters of this.component
		Map<String, String> newparams = new HashMap<String, String>();
		List<String> defn_params = this.definition.getParameters();
		if (defn_params.size() != this.parameters.size()) 
			throw new Exception("Parameters " +  defn_params +  " and " + this.parameters + " do not match.");
		for (int i = 0; i < defn_params.size(); i++) 
			newparams.put(defn_params.get(i), this.parameters.get(i));
		
		// Get the program from the definition
		Program raw_program = this.definition.getProgram(newparams, generator);
		
		// Determine the interface relabeling r
		Map<String, String> r = new HashMap<String, String>();
			
		List<String> defn_intface = this.definition.getInterface(newparams);
		List<String> refc_intface = this.intface.getInterface(parameters);
		
		// Hide all nodes outside the definition interface 
		Set<String> internalnodes = new HashSet<String>();
		for (Instance inst : raw_program.getInstances()) {
			for (Map.Entry<String, String> link : inst.getInputs().entrySet()) 
				if (!defn_intface.contains(link.getValue()))
					internalnodes.add(link.getValue());
			for (Map.Entry<String, String> link : inst.getOutputs().entrySet()) 
				if (!defn_intface.contains(link.getValue())) 
					internalnodes.add(link.getValue());
		}
		for (String node : internalnodes)
			r.put(node, generator.getNode());
		
		if (defn_intface.size() != refc_intface.size()) 
			throw new Exception("Interfaces " + defn_intface + " and " + refc_intface + " do not match.");
		
		// Substitute the new node names
		for (int i = 0; i < defn_intface.size(); i++) 
			r.put(defn_intface.get(i), refc_intface.get(i));
		
		// Rename the interface according to r
		Set<Instance> instances = new HashSet<Instance>();
		for (Instance raw : raw_program.getInstances())
			instances.add(new Instance(raw, r));
		
		return new Program(instances);
	}
}