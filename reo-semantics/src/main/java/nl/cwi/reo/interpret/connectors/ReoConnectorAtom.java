package nl.cwi.reo.interpret.connectors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.stringtemplate.v4.ST;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.Semantics;
import nl.cwi.reo.util.Monitor;

/**
 * An atomic Reo component consisting of its Reo semantics together with an 
 * optional reference to source code.
 * 
 * @param <T> Reo semantics type
 */
public final class ReoConnectorAtom<T extends Semantics<T>> implements ReoConnector<T> {
	
	/**
	 * Semantics object.
	 */
	private final T semantics;
	
	/**
	 * Reference to source code.
	 */
	private final SourceCode source;
	
	/**
	 * Set of links.
	 */
	private final Map<Port, Port> links;
	
	/**
	 * Constructs a new atomic component.
	 * @param atom		semantics
	 */
	public ReoConnectorAtom(T atom) {
		this.semantics = atom;
		this.source = new SourceCode();
		Map<Port, Port> links = new HashMap<Port, Port>();
		for (Port p : semantics.getInterface())
			links.put(p, p);
		this.links = Collections.unmodifiableMap(links);
	}
	
	/**
	 * Constructs a new atomic component.
	 * @param semantics		semantics
	 * @param source	reference to source code
	 */
	public ReoConnectorAtom(T semantics, SourceCode source) {
		this.semantics = semantics;
		this.source = source;
		Map<Port, Port> links = new HashMap<Port, Port>();
		for (Port p : semantics.getInterface())
			links.put(p, p);
		this.links = Collections.unmodifiableMap(links);
	}
	
	/**
	 * Constructs a new atomic component.
	 * @param semantics 	semantics
	 * @param source		reference to source code
	 * @param links			set of links
	 */
	public ReoConnectorAtom(T semantics, SourceCode source, Map<Port, Port> links) {
		this.semantics = semantics;
		this.source = source;
		this.links = Collections.unmodifiableMap(links);
	}
	
	/**
	 * Gets the semantics object of this atomic component.
	 * @return Semantics object
	 */
	public T getSemantics() {
		return semantics;
	}
	
	/**
	 * Gets the source code reference.
	 * @return source code reference.
	 */
	public SourceCode getSourceCode() {
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
	public ReoConnectorAtom<T> evaluate(Scope s, Monitor m) {
		return new ReoConnectorAtom<T>(semantics.evaluate(s, null), source);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReoConnector<T> reconnect(Map<Port, Port> joins) {
		return new ReoConnectorAtom<T>(semantics, source, Links.reconnect(links, joins));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReoConnector<T> renameHidden(Integer i) {
		return new ReoConnectorAtom<T>(semantics, source, Links.renameHidden(links, i));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ReoConnectorAtom<T>> flatten() {
		List<ReoConnectorAtom<T>> list = new ArrayList<ReoConnectorAtom<T>>();
		list.add(this);
		return list;
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
	public List<T> integrate() {
		List<T> list = new ArrayList<T>();
		list.add(semantics.rename(links));
		return list;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		ST st = new ST("{\n  <semantics>\n  <source>\n}");
		st.add("semantics", semantics);
		st.add("source", source);
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
}
