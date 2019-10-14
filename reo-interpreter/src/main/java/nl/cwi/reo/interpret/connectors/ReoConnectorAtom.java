package nl.cwi.reo.interpret.connectors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.stringtemplate.v4.ST;

import nl.cwi.reo.interpret.Atom;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.util.Monitor;

/**
 * An atomic Reo component consisting of its Reo semantics together with an
 * optional reference to source code.
 */
public final class ReoConnectorAtom implements ReoConnector {

	/**
	 * Component name.
	 */
	@Nullable
	private final String name;

	/**
	 * Semantics object.
	 */
	private final List<Atom> semantics;

	/**
	 * Set of links.
	 */
	private final Map<Port, Port> links;

	/**
	 * Constructs a new atomic component.
	 * 
	 * @param name
	 *            component name
	 * @param semantics
	 *            list of semantics
	 * @param links
	 *            set of links
	 */
	public ReoConnectorAtom(@Nullable String name, List<Atom> semantics, Map<Port, Port> links) {
		this.name = name;
		this.semantics = semantics;
		this.links = Collections.unmodifiableMap(links);
	}

	/**
	 * {@inheritDoc}
	 */
	@Nullable
	public String getName() {
		return name;
	}

	/**
	 * Gets the semantics objects of this atomic component.
	 * 
	 * @return List of semantics objects
	 */
	public List<Atom> getSemantics() {
		return semantics;
	}

	/**
	 * Gets the first reference of this component in a given target language, if
	 * it exists.
	 * 
	 * @param lang
	 *            target language
	 * @return The first reference in the list of semantics in the target
	 *         language, if it exists, and null otherwise.
	 */
	@Nullable
	public Reference getReference(Language lang) {
		for (Atom x : semantics){
			if (x instanceof Reference && (((Reference) x).getLanguage().equals(lang) || lang.equals(Language.TREO) || lang.equals(Language.QSHARP)))
				return (Reference) x;
		}
		return null;
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
	public ReoConnector rename(Map<Port, Port> joins) {
		if (links.isEmpty())
			return new ReoConnectorAtom(name, semantics, joins);
		return new ReoConnectorAtom(name, semantics, Links.rename(links, joins));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReoConnector flatten() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReoConnector insertNodes(boolean mergers, boolean replicators, Atom nodeFactory) {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReoConnector integrate() {
		List<Atom> _semantics = new ArrayList<>();
		for (Atom x : semantics)
			_semantics.add(x.rename(links));
		Map<Port, Port> _links = new LinkedHashMap<>();
		for (Map.Entry<Port, Port> link : links.entrySet())
			_links.put(link.getValue(), link.getValue());
		return new ReoConnectorAtom(name, _semantics, _links);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		List<String> renaming = new ArrayList<String>();
		for (Map.Entry<Port, Port> link : links.entrySet())
			renaming.add(link.getKey() + "=" + link.getValue());
		ST st = new ST("{\n  <semantics:{ x | <x>\n }>}(<renaming; separator=\", \">)");
		st.add("semantics", semantics);
		st.add("renaming", renaming);
		return st.render();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Port> getInterface() {
		return new LinkedHashSet<Port>(links.values());
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
	public List<ReoConnectorAtom> getAtoms() {
		return Arrays.asList(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReoConnector propagate(Monitor m) {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Set<Port>> getTypePartition() {
		Set<Set<Port>> partition = new HashSet<Set<Port>>();
		Set<Port> partDefault = new HashSet<Port>();
		for (Port p : links.values()) {
			if (p.getTypeTag() == null) {
				partDefault.add(p);
			} else {
				partition.add(new HashSet<Port>(Arrays.asList(p)));
			}
		}
		partition.add(partDefault);
		return partition;
	}

}
