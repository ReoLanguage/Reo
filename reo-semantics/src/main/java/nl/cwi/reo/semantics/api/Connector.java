package nl.cwi.reo.semantics.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * An ordered list of blocks of components together with a set of links.
 * @param <T> semantics object type
 */
public final class Connector<T extends Semantics<T>> implements Block<T> {
		
	/**
	 * Type of composition
	 */
	private final String operator;
	
	/**
	 * List of components
	 */
	private final List<Block<T>> components;
	
	/**
	 * Set of links
	 */
	private final Map<Port, Port> links;
	
	/**
	 * Constructs and empty connector.
	 */
	public Connector() { 
		this.operator = "";
		this.components = Collections.unmodifiableList(new ArrayList<Block<T>>());
		this.links = Collections.unmodifiableMap(new HashMap<Port, Port>());
	}
	
	/**
	 * Constructs a connector consisting of a single atom. 
	 * @param atom	atomic component
	 */
	public Connector(Component<T> atom) {
		if (atom == null)
			throw new NullPointerException();
		List<Block<T>> components = new ArrayList<Block<T>>();
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
	public Connector(String operator, List<Block<T>> components) { 
		this.operator = operator;
		this.components = Collections.unmodifiableList(components);
		Map<Port, Port> links = new HashMap<Port, Port>();
		for (Block<T> comp : components)
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
	public Connector(String operator, List<Block<T>> components, Map<Port, Port> links) { 
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
	public List<Block<T>> getComponents() {
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
	public static <T extends Semantics<T>> Connector<T> compose(String operator, List<Block<T>> components) {
		List<Block<T>> newcomponents = new ArrayList<Block<T>>();
		Integer i = Integer.valueOf(0);
		for (Block<T> comp : components)
			newcomponents.add(comp.renameHidden(i));
		return new Connector<T>(operator, newcomponents);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Connector<T> evaluate(Map<String, Expression> params) {
		List<Block<T>> newcomps = new ArrayList<Block<T>>(); 
		for (Block<T> comp : components)
			newcomps.add(comp.evaluate(params));
		return new Connector<T>(operator, newcomps);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Block<T> connect(Map<Port, Port> joins) {
		return new Connector<T>(operator, components, Links.connect(links, joins));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Block<T> renameHidden(Integer i) {
		return new Connector<T>(operator, components, Links.renameHidden(links, i));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Block<T>> flatten() {
		List<Block<T>> list = new ArrayList<Block<T>>();
		for (Block<T> comp : components)
			list.addAll(comp.flatten());
		return list;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Connector<T> insertNodes(boolean mergers, boolean replicators, T nodeFactory) {
		
		List<Block<T>> newcomponents = new ArrayList<Block<T>>();

		// Count the number of incoming and outgoing channel ends at each node.
		Map<Port, Integer> outs = new HashMap<Port, Integer>();
		Map<Port, Integer> ins = new HashMap<Port, Integer>();
		for (Block<T> comp : components) {
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
		
		for (Block<T> comp : components) {	
							
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
					System.out.println("Ports of atomic components must be known.");
					break;
				}
				
				links.put(p, pi);
				A.add(pi);
				nodes.put(p, A);
			}

			newcomponents.add(comp.insertNodes(mergers, replicators, nodeFactory).connect(links));
		}

		// Insert new instances of nodes in this list.
		for (Map.Entry<Port, SortedSet<Port>> node : nodes.entrySet()) 
			if (node.getValue().size() > 1)
				newcomponents.add(new Component<T>(nodeFactory.getNode(node.getValue())));
		
		return new Connector<T>(operator, newcomponents);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<T> integrate() {
		List<T> list = new ArrayList<T>();
		for (Block<T> comp : components)
			list.addAll(comp.integrate());
		List<T> list2 = new ArrayList<T>();
		for (T x : list2) 
			x.rename(links);
		return list2;
	}
}
