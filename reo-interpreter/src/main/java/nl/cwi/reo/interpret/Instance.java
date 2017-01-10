package nl.cwi.reo.interpret;

import java.util.HashMap;
import java.util.Map;

import nl.cwi.reo.semantics.Port;
import nl.cwi.reo.semantics.PortType;
import nl.cwi.reo.semantics.Semantics;

public final class Instance extends HashMap<String, Port> implements Evaluable<Instance> {
	
	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 6594525018732378080L;
	
	private final Semantics<?> atom;
	
	private final SourceCode source;
	
	/**
	 * Constructs a new instance from an atom.
	 * @param atom
	 */
	public Instance(Semantics<?> atom) {
		this.atom = atom;
		this.source = new SourceCode();
		for (String a : atom.getInterface()) 
			super.put(a, new Port(a));
	}
	
	public Instance(Semantics<?> atom, Map<String, Port> links, SourceCode source) {
		this.atom = atom;
		this.source = source;
		super.putAll(links);
	}
	
	/**
	 * Copy constructor.
	 * @param instance
	 */
	public Instance(Instance instance) {
		if (instance == null)
			throw new NullPointerException();
		this.atom = instance.atom;
		this.source = instance.source;
		super.putAll(new HashMap<String, Port>(instance));
	}
	
	public Semantics<?> getAtom() {
		return atom;
	}
	
	public SourceCode getSourceCode() {
		return source;
	}
	
	public void rename(Map<String, String> renaming) {
		for (Map.Entry<String, Port> link : this.entrySet()) {
			Port x = link.getValue();
			String name = renaming.get(x.getName());
			if (name == null) name = x.getName();
			link.setValue(x.rename(name));
		}
	}
	
	public void renameHidden(Integer i) {
		for (Map.Entry<String, Port> link : this.entrySet())
			if (link.getValue().isHidden())
				link.setValue(link.getValue().rename("#" + i++));
	}
	
	/**
	 * Renames the external ports, and hides all internal ports
	 * @param iface		maps external ports to new ports.
	 */
	public void instantiate(Map<Port, Port> iface) {
		for (Map.Entry<String, Port> link : this.entrySet()) {
			Port x = link.getValue();
			Port y = iface.get(x);
			if (y == null) 
				y = x.hide();
			if (x.getType() != PortType.UNKNOWN)
				y = y.retype(x.getType());
			link.setValue(y);
		}
	}

	@Override
	public Instance evaluate(Map<VariableName, Expression> params)
			throws Exception {
		return this;
	}
}
