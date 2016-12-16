package nl.cwi.reo.interpret;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * An instance of a Reo component. It consists of set of input/output links that connect input/output ports,
 * which are local to the component, to nodes that are shared with the environment. The abstract Reo semantics 
 * of this component is defined by an arbitrary {@link java.lang.Object}.
 */
public final class Atom {
	
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
	 * @param type 		type
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
	 * Restricts the interface of this atom by hiding all non-exposed nodes.
	 * @param intface		resulting interface of this atom
	 */
	public Atom restrict(Set<String> intface) {
		Map<String, String> links = new HashMap<String, String>();
		for (Map.Entry<String,String> entry : this.links.entrySet()) {
			if (!entry.getValue().startsWith("$") && !intface.contains(entry.getValue()))
				links.put(entry.getKey(),"$" + entry.getValue());
		}
		return new Atom(this.name, this.atom, links, this.types);
	}	
	
	/**
	 * Relabels the interface of this component.
	 * @param r		maps old node names to new node names.
	 */
	public Atom relabel(Map<String,String> r) {	
		Map<String, String> links = new HashMap<String, String>();
		for (Map.Entry<String, String> entry : this.links.entrySet()) {
			String node;
			if ((node = r.get(entry.getValue())) == null) 
				node = entry.getValue();
			links.put(entry.getKey(), node);
		}
		Map<String, String> types = new HashMap<String, String>();
		for (Map.Entry<String, String> entry : this.types.entrySet()) {
			String node;
			if ((node = r.get(entry.getKey())) == null) 
				node = entry.getValue();
			types.put(node, entry.getValue());
		}
		return new Atom(this.name, this.atom, links, types);
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
	public String getPortType(String port) {
		return this.types.get(port);
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
