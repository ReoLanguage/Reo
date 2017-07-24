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

import org.checkerframework.checker.nullness.qual.Nullable;
import org.stringtemplate.v4.ST;

import nl.cwi.reo.interpret.Interpretable;
import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.ports.PortType;
import nl.cwi.reo.interpret.typetags.TypeTag;
import nl.cwi.reo.util.Monitor;

// TODO: Auto-generated Javadoc
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
public final class ReoConnectorComposite<T extends Interpretable<T>> implements ReoConnector<T> {

	/**
	 * Component name.
	 */
	@Nullable
	private final String name;

	/** Type of composition. */
	private final String operator;

	/** List of components. */
	private final List<ReoConnector<T>> components;

	/** Set of links. */
	private final Map<Port, Port> links;

	/**
	 * Constructs and empty connector.
	 */
	public ReoConnectorComposite() {
		this.name = null;
		this.operator = "";
		this.components = Collections.unmodifiableList(new ArrayList<ReoConnector<T>>());
		this.links = Collections.unmodifiableMap(new HashMap<Port, Port>());
	}

	/**
	 * Constructs a new connector with a default set of links.
	 * 
	 * @param name
	 *            component name
	 * @param operator
	 *            name of product operator
	 * @param components
	 *            list of subcomponents
	 */
	public ReoConnectorComposite(@Nullable String name, String operator, List<ReoConnector<T>> components) {
		this.name = name;
		this.operator = operator;
		this.components = Collections.unmodifiableList(components);
		Map<Port, Port> links = new HashMap<Port, Port>();
		for (ReoConnector<T> C : components)
			for (Map.Entry<Port, Port> link : C.getLinks().entrySet())
				links.put(link.getValue(), link.getValue());
		this.links = Collections.unmodifiableMap(links);
	}

	/**
	 * Constructs a new connector.
	 * 
	 * @param name
	 *            component name
	 * @param operator
	 *            name of product operator
	 * @param components
	 *            list of subcomponents
	 * @param links
	 *            set of links mapping local ports to global ports
	 */
	public ReoConnectorComposite(@Nullable String name, String operator, List<ReoConnector<T>> components,
			Map<Port, Port> links) {
		this.name = name;
		for (ReoConnector<T> X : components)
			for (Port p : X.getInterface())
				if (!links.containsKey(p))
					throw new IllegalArgumentException("Port " + p + " must be linked.");
		this.operator = operator;
		this.components = Collections.unmodifiableList(components);
		this.links = Collections.unmodifiableMap(links);
	}

	/**
	 * Composes a list of Reo connectors into a composite Reo connector.
	 *
	 * @param <T>
	 *            the generic type
	 * @param operator
	 *            name of product operator
	 * @param components
	 *            list of subcomponents
	 * @return Composite Reo connector
	 */
	public static <T extends Interpretable<T>> ReoConnectorComposite<T> compose(String operator,
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
		return new ReoConnectorComposite<T>(null, operator, comps);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Nullable
	public String getName() {
		return name;
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
		return new ReoConnectorComposite<T>(name, operator, newcomps);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReoConnectorComposite<T> rename(Map<Port, Port> joins) {
		return new ReoConnectorComposite<T>(name, operator, components, Links.rename(links, joins));
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
		return new ReoConnectorComposite<T>(name, "", list, links);
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
					if (mergers && outs.get(p) != null && outs.get(p) > 1) {
						pi = p.rename(p.getName() + "_" + A.size()).hide();
						if (ins.get(p) != null && ins.get(p) == 0)
							A.add(new Port(p.getName(), PortType.IN, p.getPrioType(), p.getTypeTag(), !p.isHidden()));
					} else {
						pi = p;
					}
					break;
				case IN:
					if (replicators && ins.get(p) != null && ins.get(p) > 1) {
						pi = p.rename(p.getName() + "_" + A.size()).hide();
						if (outs.get(p) != null && outs.get(p) == 0)
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

		return new ReoConnectorComposite<T>(name, operator, newcomponents);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReoConnector<T> integrate() {
		List<ReoConnector<T>> list = new ArrayList<ReoConnector<T>>();
		for (ReoConnector<T> comp : components)
			list.add(comp.integrate());
		return new ReoConnectorComposite<T>(name, operator, list, links);
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Nullable
	public ReoConnector<T> propagate(Monitor m) {

		Map<Port, Port> r = new HashMap<Port, Port>();

		for (Set<Port> part : getTypePartition()) {
			TypeTag tag = null;
			for (Port p : part) {
				TypeTag p_tag = p.getTypeTag();
				if (p.getTypeTag() != null) {
					if (tag != null && !tag.equals(p_tag)) {
						m.add("Conflicting port types: " + tag + " and " + p_tag + ".");
						return null;
					}
					tag = p_tag;
				}
			}
			for (Port p : part)
				r.put(p, p.setTag(tag));
		}

		return new ReoConnectorComposite<T>(name, operator, components, Links.rename(links, r));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Set<Port>> getTypePartition() {
		Set<Set<Port>> partition = new HashSet<Set<Port>>();

		for (ReoConnector<T> c : components) {
			Set<Set<Port>> c_partition = c.getTypePartition();
			for (Set<Port> c_part : c_partition) {
				Set<Port> newpart = new HashSet<Port>();
				for (Port p : c_part) {
					Port q = links.get(p);
					if (q != null)
						newpart.add(q);
				}

				Set<Set<Port>> newpartition = new HashSet<>();
				for (Set<Port> part : partition) {
					if (Collections.disjoint(part, newpart)) {
						newpartition.add(part);
					} else {
						newpart.addAll(part);
					}
				}

				newpartition.add(newpart);

				partition = newpartition;
			}
		}
		return partition;
	}
}
