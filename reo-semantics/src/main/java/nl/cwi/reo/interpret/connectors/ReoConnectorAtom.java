package nl.cwi.reo.interpret.connectors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.stringtemplate.v4.ST;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.Semantics;
import nl.cwi.reo.util.Monitor;

/**
 * An atomic Reo component consisting of its Reo semantics together with an
 * optional reference to source code.
 * 
 * @param <T>
 *            Reo semantics type
 */
public final class ReoConnectorAtom<T extends Semantics<T>> implements ReoConnector<T> {

	/**
	 * Semantics object.
	 */
	private final T semantics;

	/**
	 * Reference to source code.
	 */
	private final Reference source;

	/**
	 * Set of links.
	 */
	private final Map<Port, Port> links;

	/**
	 * Constructs a new atomic component.
	 * 
	 * @param atom
	 *            semantics
	 */
	public ReoConnectorAtom(T atom) {
		this.semantics = atom;
		this.source = new Reference();
		Map<Port, Port> links = new HashMap<Port, Port>();
		for (Port p : semantics.getInterface())
			links.put(p, p);
		this.links = Collections.unmodifiableMap(links);
	}

	/**
	 * Constructs a new atomic component.
	 * 
	 * @param semantics
	 *            semantics
	 * @param source
	 *            reference to source code
	 */
	public ReoConnectorAtom(T semantics, Reference source) {
		this.semantics = semantics;
		this.source = source;
		Map<Port, Port> links = new HashMap<Port, Port>();
		for (Port p : semantics.getInterface())
			links.put(p, p);
		this.links = Collections.unmodifiableMap(links);
	}

	/**
	 * Constructs a new atomic component.
	 * 
	 * @param semantics
	 *            semantics
	 * @param source
	 *            reference to source code
	 * @param links
	 *            set of links
	 */
	public ReoConnectorAtom(T semantics, Reference source, Map<Port, Port> links) {
		this.semantics = semantics;
		this.source = source;
		this.links = Collections.unmodifiableMap(links);
	}

	/**
	 * Gets the semantics object of this atomic component.
	 * 
	 * @return Semantics object
	 */
	public T getSemantics() {
		return semantics;
	}

	/**
	 * Gets the source code reference.
	 * 
	 * @return source code reference.
	 */
	public Reference getSourceCode() {
		return source;
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
	@Nullable
	public ReoConnectorAtom<T> evaluate(Scope s, Monitor m) {
		T semantics = this.semantics.evaluate(s, m);
		if (semantics == null)
			return null;
		return new ReoConnectorAtom<T>(semantics, source);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReoConnector<T> rename(Map<Port, Port> joins) {
		return new ReoConnectorAtom<T>(semantics, source, Links.rename(links, joins));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReoConnector<T> flatten() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReoConnector<T> insertNodes(boolean mergers, boolean replicators, T nodeFactory) {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReoConnector<T> integrate() {
		return new ReoConnectorAtom<T>(semantics.rename(links), source);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		List<String> renaming = new ArrayList<String>();
		for (Map.Entry<Port, Port> link : links.entrySet())
			renaming.add(link.getKey() + "=" + link.getValue());
		ST st = new ST("{\n  <semantics>\n  <source>\n}(<renaming; separator=\", \">)");
		st.add("semantics", semantics);
		st.add("source", source);
		st.add("renaming", renaming);
		return st.render();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Port> getInterface() {
		return new HashSet<Port>(links.values());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ReoConnectorAtom<T>> getAtoms() {
		return Arrays.asList(this);
	}

}
