package nl.cwi.reo.interpret.semantics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import nl.cwi.reo.errors.CompilationException;
import nl.cwi.reo.interpret.Evaluable;
import nl.cwi.reo.interpret.ranges.Expression;
import nl.cwi.reo.interpret.variables.VariableName;
import nl.cwi.reo.semantics.Port;
import nl.cwi.reo.semantics.PortType;
import nl.cwi.reo.semantics.Semantics;

public class ComponentList<T extends Semantics<T>> extends ArrayList<Component<T>> implements Evaluable<ComponentList<T>> {
	
	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = -4691832430455957713L;
	
	/**
	 * Type of composition
	 */
	private final String operator;
	
	/**
	 * Counter for generating fresh port names.
	 */
	private int i;
	
	/**
	 * Constructs and empty list of instances.
	 */
	public ComponentList() { 
		this.operator = "";
	}
	
	/**
	 * Constructs a singleton list with 
	 * @param atom
	 */
	public ComponentList(T atom) {
		if (atom == null)
			throw new NullPointerException();
		super.add(new Component<T>(atom));
		this.operator = "";
	}
	
	/**
	 * Copy constructor.
	 * @param instances
	 */
	public ComponentList(ComponentList<T> instances) {
		if (instances == null)
			throw new NullPointerException();
		for (Component<T> inst : instances) {
			if (inst == null)
				throw new NullPointerException();
			super.add(new Component<T>(inst));
		}
		this.operator = instances.operator;
	}
	
	/**
	 * Copy constructor.
	 * @param instances
	 */
	public ComponentList(List<Component<T>> instances, String operator) {
		if (instances == null)
			throw new NullPointerException();
		for (Component<T> inst : instances) {
			if (inst == null)
				throw new NullPointerException();
			super.add(new Component<T>(inst));
		}
		this.operator = operator;
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

	@Override
	public ComponentList<T> evaluate(Map<VariableName, Expression> params) throws CompilationException {
		ComponentList<T> _instances = new ComponentList<T>(this); 
		for (Component<T> comp : _instances)
			comp.evaluate(params);
		return _instances;
	}
}
