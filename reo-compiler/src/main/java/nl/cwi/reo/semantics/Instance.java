package nl.cwi.reo.semantics;

import java.util.HashMap;
import java.util.Map;

/**
 * An instance of a Reo component. It consists of set of input/output links that connect input/output ports,
 * which are local to the component, to nodes that are shared with the environment. The abstract Reo semantics 
 * of this component is defined by an arbitrary {@link java.lang.Object}.
 */
public final class Instance {
	
	/**
	 * Name of this instance.
	 */
	private final String name;
	
	/**
	 * Semantics of this component instance.
	 */
	private final Object atom;
	
	/**
	 * Maps each local input port of this component to a global node name
	 */
	private final Map<String, String> input;
	
	/**
	 * Maps each local output port of this component to a global node name
	 */
	private final Map<String, String> output;
	
	/**
	 * Constructor.
	 * @param atom		semantics
	 * @param input		input nodes
	 * @param output	output nodes
	 */
	public Instance(String name, Object atom, Map<String, String> input, Map<String, String> output) {
		this.name = name;
		this.atom = atom;
		this.input = input;
		this.output = output;
	}
	
	/**
	 * Copies the component and renames attached nodes.
	 * @param A		original component
	 * @param r		renaming of attached nodes
	 */
	public Instance(Instance A, Map<String, String> r) {
		Map<String, String> input = new HashMap<String, String>();
		for (Map.Entry<String, String> entry : A.input.entrySet()) {
			String node;
			if ((node = r.get(entry.getValue())) == null) 
				node = entry.getValue();
			input.put(entry.getKey(), node);
		}
		Map<String, String> output = new HashMap<String, String>();
		for (Map.Entry<String, String> entry : A.output.entrySet()) {
			String node;
			if ((node = r.get(entry.getValue())) == null) 
				node = entry.getValue();
			output.put(entry.getKey(), node);
		}
		this.name = A.name;
		this.atom = A.atom;
		this.input = input;
		this.output = output;
	}
	
	/**
	 * Gets the input node links.
	 * @return map from input ports to nodes
	 */
	public Map<String, String> getInputs() {
		return this.input;
	}
	
	/**
	 * Gets the output node links.
	 * @return map from output ports to nodes
	 */
	public Map<String, String> getOutputs() {
		return this.output;
	}
	
	/**
	 * Gets the atomic component.
	 * @return component that defines the semantics of the component
	 */
	public Object getComponent() {
		return atom;
	} 
	
	/**
	 * Gets the string representation of a component instance.
	 */
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("Name         : " + name + "\n");
		str.append("Input nodes  : " + input + "\n");
		str.append("Output nodes : " + output + "\n");
		str.append("ReoSemantics : " + atom + "\n");		
		return str.toString();
	}
	
}
