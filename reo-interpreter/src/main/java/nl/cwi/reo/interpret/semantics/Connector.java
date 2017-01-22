package nl.cwi.reo.interpret.semantics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import nl.cwi.reo.errors.CompilationException;
import nl.cwi.reo.interpret.Evaluable;
import nl.cwi.reo.interpret.ranges.Expression;
import nl.cwi.reo.interpret.variables.VariableName;
import nl.cwi.reo.semantics.Port;
import nl.cwi.reo.semantics.PortType;
import nl.cwi.reo.semantics.Semantics;

public final class Connector<T extends Semantics<T>> implements Component1<T> {
		
	/**
	 * Type of composition
	 */
	private final String operator;
	
	/**
	 * List of components
	 */
	private final List<Component1<T>> components;
	
	/**
	 * Collection of nodes
	 */
	private final Map<String, nl.cwi.reo.interpret.semantics.Port<T>> nodes;
	
	/**
	 * Counter for generating fresh node names.
	 */
	private int i;
	
	/**
	 * Constructs and empty connector.
	 */
	public Connector() { 
		this.operator = "";
		this.components = Collections.unmodifiableList(new ArrayList<Component1<T>>());
		this.nodes = Collections.unmodifiableMap(new HashMap<String, nl.cwi.reo.interpret.semantics.Port<T>>());
	}
	
	/**
	 * Constructs and new connector.
	 */
	public Connector(String operator, List<Component1<T>> components, Map<String, nl.cwi.reo.interpret.semantics.Port<T>> nodes) { 
		this.operator = operator;
		this.components = Collections.unmodifiableList(components);
		this.nodes = Collections.unmodifiableMap(nodes);
	}
	
	/**
	 * Constructs a connector consisting of a single atom. 
	 * @param atom	atomic component
	 */
	public Connector(Atom<T> atom) {
		if (atom == null)
			throw new NullPointerException();
		List<Component1<T>> components = new ArrayList<Component1<T>>();
		components.add(atom);
		this.operator = "";
		this.components = Collections.unmodifiableList(components);
		Map<String, nl.cwi.reo.interpret.semantics.Port<T>> nodes = new HashMap<String, nl.cwi.reo.interpret.semantics.Port<T>>();
		for (nl.cwi.reo.interpret.semantics.Port<T> p : atom.getInterface())
			nodes.a
		this.nodes = Collections.unmodifiableMap(new HashMap<String, nl.cwi.reo.interpret.semantics.Port<T>>());
	}
	
	public String getOperator() {
		return operator;
	}
	
	/**
	 * Inserts, if necessary, a merger and/or replicator at every node in this instance list. 
	 * @param mergers			insert mergers
	 * @param replicators		insert replicators
	 */
	public void insertNodes(boolean mergers, boolean replicators) {
	
		if (this.size() == 0)
			return;

		// Count the number of incoming and outgoing channel ends at each node.
		Map<Port, Integer> outs = new HashMap<Port, Integer>();
		Map<Port, Integer> ins = new HashMap<Port, Integer>();
		for (Component<T> inst : this) {
			for (Map.Entry<Port, Port> link : inst.entrySet()) {
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
		
		for (Component<T> inst : this) {	
							
			Map<Port, Port> links = new HashMap<Port, Port>();

			// For every port of this component, add the current node size as a suffix.
			for (Map.Entry<Port, Port> link : inst.entrySet()) {
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
						if (ins.get(p) == 0) A.add(new Port(p.getName(), PortType.IN, p.getTypeTag(), p.isHidden()));
					} else {
						pi = p;
					}
					break;
				case IN:
					if (replicators && ins.get(p) > 1) {
						pi = p.rename(p.getName() + "." + A.size());
						if (outs.get(p) == 0) A.add(new Port(p.getName(), PortType.OUT, p.getTypeTag(), p.isHidden()));
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

	    	inst.joinAndHide(links);
		}

		// Insert new instances of nodes in this list.
		T unit = this.get(0).getAtom();
		for (Map.Entry<Port, SortedSet<Port>> node : nodes.entrySet()) 
			if (node.getValue().size() > 1)
				this.add(new Component<T>(unit.getNode(node.getValue())));
	}
	
	public List<T> getComponents() {
		List<T> list = new ArrayList<T>();
		for (Component<T> inst : this)
			list.add(inst.getAtom().rename(inst));
		return list;
	}
	
	/**
	 * Composes this list of instances with another list of instances, and
	 * renames all hidden ports to avoid sharing of internal ports.
	 * @param list 		other list of instances
	 */
	public void compose(ComponentList<T> list) {
		i = this.renameHidden(i);
		list.renameHidden(i);
		this.addAll(list);
	}
	
	/**
	 * Renames all hidden ports in this list of instances to an integer value, 
	 * starting from a given integer i.
	 * @param i		start value of hidden ports.
	 * @return the smallest integer greater or equal to i, that not used as a port name.
	 */
	private int renameHidden(int i) {
		Map<Port, Port> links = new HashMap<Port, Port>();
		for (Component<T> comp : this) {
			for (Map.Entry<Port, Port> link : comp.entrySet()) {
				Port x = link.getValue();
				if (!links.containsKey(x)) {
					if (x.isHidden()) {
						links.put(x, new Port("#" + i++));
					} else {
						links.put(x, x);
					}
				}
			}
			comp.joinAndHide(links);
		}
		return i;
	}

//	@Override
//	public ComponentList<T> evaluate(Map<VariableName, Expression> params) throws CompilationException {
//		ComponentList<T> _instances = new ComponentList<T>(this); 
//		for (Component<T> comp : _instances)
//			comp.evaluate(params);
//		return _instances;
//	}

	@Override
	public Component1<T> evaluate(Map<VariableName, Expression> params) throws CompilationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<nl.cwi.reo.interpret.semantics.Port<T>> getInterface() {
		// TODO Auto-generated method stub
		return null;
	}
}
