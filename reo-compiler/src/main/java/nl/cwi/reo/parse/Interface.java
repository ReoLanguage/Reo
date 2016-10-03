package nl.cwi.reo.parse;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A parameterized interface, such as, for example, (a, b[k], c[n-1...23]).
 */
public final class Interface {

	/**
	 * A map that assigns to each node an empty list, a list that contains a single index, or a list 
	 * that contains a lower index and an upper index. For example, <code>{a=[], b=[k], c=[n-1, 23]}</code> 
	 * represents the interface (a, b[k], c[n-1...23]).
	 */
	private final List<Nodes> intface;
	
	/**
	 * Constructs a new empty interface.
	 */
	public Interface() {
		this.intface = new ArrayList<Nodes>();
	}

	/**
	 * Constructs interface.
	 * @param intface	interface
	 */
	public Interface(List<Nodes> intface) {
		this.intface = intface;
	}
	
	/**
	 * Constructs a new singleton interface (name).
	 * @param name		node name
	 */
	public Interface(String name) {
		this.intface = new ArrayList<Nodes>();
		this.intface.add(new Nodes(name));
	}

	/**
	 * Constructs a new singleton interface (name[expr]).
	 * @param name		node name
	 * @param expr		index expression
	 */
	public Interface(String name, Expression expr) {
		this.intface = new ArrayList<Nodes>();
		this.intface.add(new Nodes(name, expr));
	}

	/**
	 * Constructs a new interface (name[lower...upper]).
	 * @param name		node name
	 * @param lower		lower index expression
	 * @param upper		upper index expression
	 */
	public Interface(String name, Expression lower, Expression upper) {
		this.intface = new ArrayList<Nodes>();
		this.intface.add(new Nodes(name, lower, upper));
	}
	
	/**
	 * Joins two abstract interfaces into a single abstract interface
	 * @param intface1		abstract interface
	 * @param intface2		abstract interface
	 * @return composed abstract interface.
	 */
	public static Interface join(Interface intface1, Interface intface2) {
		List<Nodes> intface = new ArrayList<Nodes>(intface1.intface);
		intface.addAll(intface2.intface);
		return new Interface(intface);
	}
	
	/**
	 * Gets a concrete interface.
	 * @param parameters		parameter assignment
	 * @return list of node names.
	 * @throws Exception if not all required parameters are defined.
	 */
	public List<String> getInterface(Map<String, String> parameters) throws Exception {
		List<String> nodes = new ArrayList<String>();
		for (Nodes A : intface) 
			nodes.addAll(A.getNodes(parameters));
		Set<String> uniqueNodes = new HashSet<String>(nodes);
		if(uniqueNodes.size() < nodes.size()) 
			throw new Exception("Interface "  + intface + " contains duplicate nodes for parameters " + parameters + ".");
		return nodes;
	}
	
	/**
	 * Gets the string representation of this parameterized interface.
	 */
	@Override
	public String toString() {
		return intface.toString();
	}
}
