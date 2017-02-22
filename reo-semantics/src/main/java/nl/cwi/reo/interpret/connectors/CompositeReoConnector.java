package nl.cwi.reo.interpret.connectors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.stringtemplate.v4.ST;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.ports.PortType;
import nl.cwi.reo.util.Monitor;

/**
 * <p>The semantics of Reo connectors specified in .treo files.
 * This class consists of an ordered list of blocks of components together with a set of links.
 * 
 * <p>This class is immutable.
 * 
 * @param <T> semantics object type
 */
public final class CompositeReoConnector<T extends Semantics<T>> implements ReoConnector<T> {
		
	/**
	 * Type of composition
	 */
	private final String operator;
	
	/**
	 * List of components
	 */
	private final List<ReoConnector<T>> components;
	
	/**
	 * Set of links
	 */
	private final Map<Port, Port> links;
	
	/**
	 * Constructs and empty connector.
	 */
	public CompositeReoConnector() { 
		this.operator = "";
		this.components = Collections.unmodifiableList(new ArrayList<ReoConnector<T>>());
		this.links = Collections.unmodifiableMap(new HashMap<Port, Port>());
	}
	
	/**
	 * Constructs a connector consisting of a single atom. 
	 * @param atom	atomic component
	 */
	public CompositeReoConnector(AtomicReoConnector<T> atom) {
		if (atom == null)
			throw new NullPointerException();
		List<ReoConnector<T>> components = new ArrayList<ReoConnector<T>>();
		components.add(atom);
		this.operator = "";
		this.components = Collections.unmodifiableList(components);
		Map<Port, Port> links = new HashMap<Port, Port>();
		for (Map.Entry<Port, Port> link : atom.getLinks().entrySet()) 
			links.put(link.getValue(), link.getValue());
		this.links = Collections.unmodifiableMap(links);
	}
	
	/**
	 * Constructs a new connector with a default set of links.
	 * @param operator		name of product operator
	 * @param components	list of subcomponents
	 */
	public CompositeReoConnector(String operator, List<ReoConnector<T>> components) { 
		this.operator = operator;
		this.components = Collections.unmodifiableList(components);
		Map<Port, Port> links = new HashMap<Port, Port>();
		for (ReoConnector<T> comp : components)
			for (Map.Entry<Port, Port> link : comp.getLinks().entrySet()) 
				links.put(link.getValue(), link.getValue());
		this.links = Collections.unmodifiableMap(links);
	}

	/**
	 * Constructs a new connector.
	 * @param operator		name of product operator
	 * @param components	list of subcomponents
	 * @param links			set of links mapping local ports to global ports
	 */
	public CompositeReoConnector(String operator, List<ReoConnector<T>> components, Map<Port, Port> links) { 
		this.operator = operator;
		this.components = Collections.unmodifiableList(components);
		this.links = Collections.unmodifiableMap(links);
	}
	
	/**
	 * Gets the name of the product operator that must be used to compose this connector.
	 * @return name of product operator.
	 */
	public String getOperator() {
		return operator;
	}
	
	/**
	 * Gets the list of all subcomponents in this connector.
	 * @return list of subcomponents.
	 */
	public List<ReoConnector<T>> getComponents() {
		return components;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<Port, Port> getLinks() {
		return links;
	}
	
	/**
	 * Composes this list of instances with another list of instances, and
	 * renames all hidden ports to avoid sharing of internal ports.
	 * @param operator			name of the operator
	 * @param components 		other list of instances
	 */
	public static <T extends Semantics<T>> CompositeReoConnector<T> compose(String operator, List<? extends ReoConnector<T>> components) {
		List<ReoConnector<T>> newcomponents = new ArrayList<ReoConnector<T>>();
		Integer i = Integer.valueOf(0);
		for (ReoConnector<T> comp : components)
			newcomponents.add(comp.renameHidden(i));
		return new CompositeReoConnector<T>(operator, newcomponents);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Deprecated
	public CompositeReoConnector<T> evaluate(Scope s, Monitor m) {
		List<ReoConnector<T>> newcomps = new ArrayList<ReoConnector<T>>(); 
		for (ReoConnector<T> comp : components)
			newcomps.add(comp.evaluate(s, null));
		return new CompositeReoConnector<T>(operator, newcomps);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public CompositeReoConnector<T> reconnect(Map<Port, Port> joins) {
		return new CompositeReoConnector<T>(operator, components, Links.reconnect(links, joins));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CompositeReoConnector<T> renameHidden(Integer i) {
		return new CompositeReoConnector<T>(operator, components, Links.renameHidden(links, i));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<AtomicReoConnector<T>> flatten() {
		List<AtomicReoConnector<T>> list = new ArrayList<AtomicReoConnector<T>>();
		for (ReoConnector<T> comp : components)
			list.addAll(comp.flatten());
		return list;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public CompositeReoConnector<T> insertNodes(boolean mergers, boolean replicators, T nodeFactory) {
		
		List<ReoConnector<T>> newcomponents = new ArrayList<ReoConnector<T>>();

		// Count the number of incoming and outgoing channel ends at each node.
		Map<Port, Integer> outs = new HashMap<Port, Integer>();
		Map<Port, Integer> ins = new HashMap<Port, Integer>();
		for (ReoConnector<T> comp : components) {
			for (Map.Entry<Port, Port> link : comp.getLinks().entrySet()) {
				Port p = link.getValue();
				Integer out = outs.get(p);
				if (out == null) {
					out = new Integer(0);
					outs.put(p, out);
				}				
				if (p.getType() == PortType.OUT) outs.put(p, ++out);
				Integer in = ins.get(p);
				if (in == null) {
					in = new Integer(0);
					ins.put(p, in);
				}
				if (p.getType() == PortType.IN) ins.put(p, ++in);
			}
		}
		
		// Split shared ports in every atom in main, and insert a node
		Map<Port, SortedSet<Port>> nodes = new HashMap<Port, SortedSet<Port>>();
		
		for (ReoConnector<T> comp : components) {	
							
			Map<Port, Port> links = new HashMap<Port, Port>();

			// For every port of this component, add the current node size as a suffix.
			for (Map.Entry<Port, Port> link : comp.getLinks().entrySet()) {
				Port p = link.getValue();
				Port pi = null;
				
				// Get the current node A of this port, or create a new node A.
				SortedSet<Port> A = nodes.get(p);
				if (A == null) {
					A = new TreeSet<Port>();
					nodes.put(p, A);
				}
				
				// Find the correct renaming pi of port p. 
				switch (p.getType()) {
				case OUT:
					if (mergers && outs.get(p) > 1) {
						pi = p.rename(p.getName() + "." + A.size());
						if (ins.get(p) == 0) 
							A.add(new Port(p.getName(), PortType.IN, p.getPrioType(), p.getTypeTag(), p.isHidden()));
					} else {
						pi = p;
					}
					break;
				case IN:
					if (replicators && ins.get(p) > 1) {
						pi = p.rename(p.getName() + "." + A.size());
						if (outs.get(p) == 0) 
							A.add(new Port(p.getName(), PortType.OUT, p.getPrioType(), p.getTypeTag(), p.isHidden()));
					} else {
						pi = p;
					}
					break;
				default:
					throw new RuntimeException("Port type must be input or output.");
				}
				
				links.put(p, pi);
				A.add(pi);
				nodes.put(p, A);
			}

			newcomponents.add(comp.insertNodes(mergers, replicators, nodeFactory).reconnect(links));
		}

		// Insert new instances of nodes in this list.
		for (Map.Entry<Port, SortedSet<Port>> node : nodes.entrySet()) 
			if (node.getValue().size() > 1)
				newcomponents.add(new AtomicReoConnector<T>(nodeFactory.getNode(node.getValue())));
		
		return new CompositeReoConnector<T>(operator, newcomponents);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<T> integrate() {
		List<T> list = new ArrayList<T>();
		for (ReoConnector<T> comp : components)
			list.addAll(comp.integrate());
		List<T> list2 = new ArrayList<T>();
		for (T x : list2) 
			x.rename(links);
		return list2;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		ST st = new ST("<operator>{\n  <components; separator=\"\n\">\n}");
		st.add("operator", operator);
		st.add("components", components);
		return st.render();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Port> getInterface() {
		return new HashSet<Port>(links.values());
	}
}
