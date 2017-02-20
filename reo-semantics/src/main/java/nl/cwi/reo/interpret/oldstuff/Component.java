package nl.cwi.reo.interpret.oldstuff;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import nl.cwi.reo.interpret.strings.StringValue;

public final class Component<T extends Semantics<T>> implements Block<T> {
	
	private final T semantics;
	
	private final SourceCode source;
	
	private final Map<Port, Port> links;
	
	/**
	 * Constructs a new atomic component.
	 * @param atom		semantics
	 */
	public Component(T atom) {
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
	public Component(T semantics, SourceCode source) {
		this.semantics = semantics;
		this.source = source;
		Map<Port, Port> links = new HashMap<Port, Port>();
		for (Port p : semantics.getInterface())
			links.put(p, p);
		this.links = Collections.unmodifiableMap(links);
	}
	
	public Component(T semantics, SourceCode source, Map<Port, Port> links) {
		this.semantics = semantics;
		this.source = source;
		this.links = Collections.unmodifiableMap(links);
	}
	
	/**
	 * Gets the semantics object of this atomic componnet.
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
	public Component<T> evaluate(Map<String, Expression> params) {
		Map<String, String> p = new HashMap<String, String>();
		for (Map.Entry<String, Expression> def : params.entrySet())
			if (def.getValue() instanceof Object)//StringValue)
				p.put(def.getKey(), ((Object)def.getValue()).toString());
		return new Component<T>(semantics.evaluate(p), source);
	}

	/**
	  * {@inheritDoc}
	  */
	@Override
	public Block<T> connect(Map<Port, Port> joins) {
		return new Component<T>(semantics, source, Links.connect(links, joins));
	}

	/**
	  * {@inheritDoc}
	  */
	@Override
	public Block<T> renameHidden(Integer i) {
		return new Component<T>(semantics, source, Links.renameHidden(links, i));
	}

	/**
	  * {@inheritDoc}
	  */
	@Override
	public List<Component<T>> flatten() {
		List<Component<T>> list = new ArrayList<Component<T>>();
		list.add(this);
		return list;
	}

	/**
	  * {@inheritDoc}
	  */
	@Override
	public Block<T> insertNodes(boolean mergers, boolean replicators, T nodeFactory) {
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
}
