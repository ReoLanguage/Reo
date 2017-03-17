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
import nl.cwi.reo.semantics.Semantics;
import nl.cwi.reo.util.Monitor;

/**
 * <p>
 * The semantics of Reo connectors specified in .treo files. This class consists
 * of an ordered list of blocks of components together with a set of links.
 * 
 * <p>
 * This class is immutable.
 * 
 * @param <T>
 *            semantics object type
 */
public final class ReoConnectorComposite<T extends Semantics<T>> implements ReoConnector<T> {

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
	public ReoConnectorComposite() {
		this.operator = "";
		this.components = Collections.unmodifiableList(new ArrayList<ReoConnector<T>>());
		this.links = Collections.unmodifiableMap(new HashMap<Port, Port>());
	}

	/**
	 * Constructs a new connector with a default set of links.
	 * 
	 * @param operator
	 *            name of product operator
	 * @param components
	 *            list of subcomponents
	 */
	public ReoConnectorComposite(String operator, List<ReoConnector<T>> components) {
		this.operator = operator;
		this.components = Collections.unmodifiableList(components);
		Map<Port, Port> links = new HashMap<Port, Port>();
		for (ReoConnector<T> C : components)
			for (Map.Entry<Port, Port> link : C.getLinks().entrySet())
				links.put(link.getValue(), link.getValue());
		this.links = Collections.unmodifiableMap(links);
	}

	/**
	 * Composes a list of Reo connectors into a composite Reo connector.
	 * 
	 * @param operator
	 *            name of product operator
	 * @param components
	 *            list of subcomponents
	 * @return Composite Reo connector
	 */
	public static <T extends Semantics<T>> ReoConnectorComposite<T> compose(String operator,
			List<ReoConnector<T>> components) {
		int i = 1;
		List<ReoConnector<T>> comps = new ArrayList<ReoConnector<T>>();
		for (ReoConnector<T> C : components) {
			Map<Port, Port> r = new HashMap<Port, Port>();
			for (Map.Entry<Port, Port> link : C.getLinks().entrySet()) {
				Port p = link.getValue();
				r.put(p, p.isHidden() ? p.rename("_" + i++) : p);
			}
			comps.add(C.rename(r));
		}
		return new ReoConnectorComposite<T>(operator, comps);
	}

	/**
	 * Constructs a new connector.
	 * 
	 * @param operator
	 *            name of product operator
	 * @param components
	 *            list of subcomponents
	 * @param links
	 *            set of links mapping local ports to global ports
	 */
	public ReoConnectorComposite(String operator, List<ReoConnector<T>> components, Map<Port, Port> links) {
		for (ReoConnector<T> X : components)
			for (Port p : X.getInterface())
				if (!links.containsKey(p))
					throw new IllegalArgumentException("Port " + p + " must be linked.");
		this.operator = operator;
		this.components = Collections.unmodifiableList(components);
		this.links = Collections.unmodifiableMap(links);
	}

	/**
	 * Gets the name of the product operator that must be used to compose this
	 * connector.
	 * 
	 * @return name of product operator.
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * Gets the list of all subconnectors in this connector.
	 * 
	 * @return list of subconnectors.
	 */
	public List<ReoConnector<T>> getComponents() {
		return components;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ReoConnectorAtom<T>> getAtoms() {
		List<ReoConnectorAtom<T>> atoms = new ArrayList<ReoConnectorAtom<T>>();
		for (ReoConnector<T> c : components)
			if (c instanceof ReoConnectorAtom<?>)
				atoms.add((ReoConnectorAtom<T>) c);
			else
				atoms.addAll(c.getAtoms());
		return atoms;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<Port, Port> getLinks() {
		return links;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReoConnectorComposite<T> evaluate(Scope s, Monitor m) {
		List<ReoConnector<T>> newcomps = new ArrayList<ReoConnector<T>>();
		for (ReoConnector<T> comp : components) {
			ReoConnector<T> e = comp.evaluate(s, m);
			if (e != null)
				newcomps.add(e);
		}
		return new ReoConnectorComposite<T>(operator, newcomps);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReoConnectorComposite<T> rename(Map<Port, Port> joins) {
		return new ReoConnectorComposite<T>(operator, components, Links.rename(links, joins));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReoConnectorComposite<T> flatten() {
		List<ReoConnector<T>> list = new ArrayList<ReoConnector<T>>();
		for (ReoConnector<T> comp : components) {
			ReoConnector<T> rc = comp.rename(links).flatten();
			if (rc instanceof ReoConnectorComposite<?>)
				list.addAll(((ReoConnectorComposite<T>) rc).components);
			else
				list.add(rc);
		}
		Map<Port, Port> links = new HashMap<Port, Port>();
		for (ReoConnector<T> X : list)
			for (Port p : X.getInterface())
				links.put(p, p);
		return new ReoConnectorComposite<T>("", list, links);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReoConnectorComposite<T> insertNodes(boolean mergers, boolean replicators, T nodeFactory) {

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
				if (p.getType() == PortType.OUT)
					outs.put(p, ++out);
				Integer in = ins.get(p);
				if (in == null) {
					in = new Integer(0);
					ins.put(p, in);
				}
				if (p.getType() == PortType.IN)
					ins.put(p, ++in);
			}
		}

		// Split shared ports in every atom in main, and insert a node
		Map<Port, SortedSet<Port>> nodes = new HashMap<Port, SortedSet<Port>>();

		for (ReoConnector<T> comp : components) {

			Map<Port, Port> links = new HashMap<Port, Port>();

			// For every port of this component, add the current node size as a
			// suffix.
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
					// if (mergers && new Integer(1).compareTo(outs.get(p)) > 0)
					// {
					if (mergers && outs.get(p) > 1) {
						pi = p.rename(p.getName() + "." + A.size()).hide();
						if (ins.get(p) == 0)
							// if (new Integer(0).equals(ins.get(p)))
							A.add(new Port(p.getName(), PortType.IN, p.getPrioType(), p.getTypeTag(), !p.isHidden()));
					} else {
						pi = p;
					}
					break;
				case IN:
					if (replicators && ins.get(p) > 1) {
						// if (replicators && new
						// Integer(1).compareTo(ins.get(p)) > 0) {
						pi = p.rename(p.getName() + "." + A.size()).hide();
						if (outs.get(p) == 0)
							// if (new Integer(0).equals(outs.get(p)))
							A.add(new Port(p.getName(), PortType.OUT, p.getPrioType(), p.getTypeTag(), !p.isHidden()));
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

			newcomponents.add(comp.insertNodes(mergers, replicators, nodeFactory).rename(links));
		}

		// Insert new instances of nodes in this list.
		for (Map.Entry<Port, SortedSet<Port>> node : nodes.entrySet())
			if (node.getValue().size() > 1)
				newcomponents.add(new ReoConnectorAtom<T>(nodeFactory.getNode(node.getValue())));

		return new ReoConnectorComposite<T>(operator, newcomponents);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReoConnector<T> integrate() {
		List<ReoConnector<T>> list = new ArrayList<ReoConnector<T>>();
		for (ReoConnector<T> comp : components)
			list.add(comp.integrate());
		return new ReoConnectorComposite<T>(operator, list, links);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		List<String> renaming = new ArrayList<String>();
		for (Map.Entry<Port, Port> link : links.entrySet())
			renaming.add(link.getKey() + "=" + link.getValue());
		ST st = new ST("<operator>{\n  <components; separator=\"\n\">\n}(<renaming; separator=\", \">)");
		st.add("operator", operator);
		st.add("components", components);
		st.add("renaming", renaming);
		return st.render();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Port> getInterface() {
		Set<Port> set = new HashSet<Port>();
		for (Port p : links.values())
			if (!p.isHidden())
				set.add(p);
		return new HashSet<Port>(set);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return components.isEmpty();
	}
}
