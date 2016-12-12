package nl.cwi.reo.semantics;

import java.util.HashMap;
import java.util.Map;

/**
 * An instance of a Reo component. It consists of set of input/output links that connect input/output ports,
 * which are local to the component, to nodes that are shared with the environment. The abstract Reo semantics 
 * of this component is defined by an arbitrary {@link java.lang.Object}.
 */
public class Atom {
	
	/**
	 * Name of this instance.
	 */
	private final String name;
	
	/**
	 * Semantics of this component instance.
	 */
	private final Object atom;
	
	/**
	 * Maps each port of this component to a node name
	 */
	private final Map<String, String> links;
	
	/**
	 * Maps each port of this component to its type
	 */
	private final Map<String, String> types;
	
	/**
	 * Constructor.
	 * @param name 		name
	 * @param atom		semantics
	 * @param links		map from ports to nodes
	 * @param types 	map from ports to types
	 */
	public Atom(String name, Object atom, Map<String, String> links, Map<String, String> types) {
		this.name = name;
		this.atom = atom;
		this.links = links;
		this.types = types;
	}
	
	/**
	 * Copies the component and renames attached nodes.
	 * @param A		original component
	 * @param r		renaming of attached nodes
	 */
	public Atom(Atom A, Map<String, String> r) {
		Map<String, String> links = new HashMap<String, String>();
		for (Map.Entry<String, String> entry : A.links.entrySet()) {
			String node;
			if ((node = r.get(entry.getValue())) == null) 
				node = entry.getValue();
			links.put(entry.getKey(), node);
		}
		Map<String, String> types = new HashMap<String, String>();
		for (Map.Entry<String, String> entry : A.types.entrySet()) {
			String node;
			if ((node = r.get(entry.getKey())) == null) 
				node = entry.getValue();
			types.put(node, entry.getValue());
		}
		this.name = A.name;
		this.atom = A.atom;
		this.links = links;
		this.types = types;
	}
	
	/**
	 * Adds a suffix to internal nodes
	 * @param suffix 		string added to the end of each internal node
	 */
	public void addSuffixToHidden(String suffix) {
		for (Map.Entry<String, String> entry : links.entrySet()) {
			if (entry.getValue().startsWith("$")) 
				entry.setValue(entry.getKey() + suffix);
		}
	}
	
	/**
	 * Gets the ports to nodes links.
	 * @return map from port names to node names
	 */
	public Map<String, String> getLinks() {
		return this.links;
	}
	
	/**
	 * Gets the type of each port.
	 * @return map from port names to types
	 */
	public Map<String, String> getTypes() {
		return this.types;
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
		str.append("Name   : " + name + "\n");
		str.append("Nodes  : " + links + "\n");
		str.append("Types  : " + types + "\n");
		str.append("Atom   : " + atom + "\n");		
		return str.toString();
	}
	
}
