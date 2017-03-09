package nl.cwi.reo.semantics.plain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.Semantics;
import nl.cwi.reo.semantics.SemanticsType;
import nl.cwi.reo.util.Monitor;

/**
 * A work automaton.
 */
public final class Plain implements Semantics<Plain> {

	/**
	 * Component name.
	 */
	private final String name;

	/**
	 * Ports in interface.
	 */
	private final List<Port> ports;

	/**
	 * Constructs a plain Reo component with given component name and ports
	 * names in the interface
	 * 
	 * @param name
	 *            component name
	 * @param ports
	 *            list of ports names in interface
	 */
	public Plain(String name, String... ports) {
		this.name = name;
		List<Port> list = new ArrayList<Port>();
		for (int i = 0; i < ports.length; i++)
			list.add(new Port(ports[i]));
		this.ports = list;
	}
	
	/**
	 * Constructs a plain Reo component with a given component name and ports
	 * names in the interface 
	 * @param name
	 *            component name
	 * @param ports
	 *            list of ports names in interface
	 */
	public Plain(String name, Collection<Port> ports) {
		this.name = name;
		this.ports = new ArrayList<Port>(ports);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Plain getNode(Set<Port> node) {
		return new Plain("node", node);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Port> getInterface() {
		return new HashSet<Port>(ports);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Plain rename(Map<Port, Port> links) {
		List<Port> list = new ArrayList<Port>();
		for (Port p : ports) {
			Port q = null;
			if ((q = links.get(p)) != null)
				list.add(q);
			else 
				list.add(p);
		}
		return new Plain(name, list);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return name + ports;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Plain compose(List<Plain> components) {
		String name = "";
		List<Port> ports = new ArrayList<Port>();
		for (Plain x : components) {
			name += x.name;
			ports.addAll(x.ports);
		}
		return new Plain(name, ports);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Plain evaluate(Scope s, Monitor m) {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Plain restrict(Collection<? extends Port> intface) {
		List<Port> list = new ArrayList<Port>();
		for (Port p : ports)
			if (intface.contains(p))
				list.add(p);
		return new Plain(name, list);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SemanticsType getType() {
		return SemanticsType.PLAIN;
	}
}
