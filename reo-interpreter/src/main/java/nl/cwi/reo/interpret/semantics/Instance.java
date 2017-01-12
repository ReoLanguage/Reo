package nl.cwi.reo.interpret.semantics;

import java.util.HashMap;
import java.util.Map;

import nl.cwi.reo.interpret.Evaluable;
import nl.cwi.reo.interpret.arrays.Expression;
import nl.cwi.reo.interpret.variables.VariableName;
import nl.cwi.reo.semantics.Port;
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
	 * @param atom		semantics object
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
	
	/**
	 * Renames the external ports, and hides all internal ports
	 * @param links		maps external ports to new ports.
	 */
	public void join(Map<Port, Port> links) {
		for (Map.Entry<String, Port> link : this.entrySet()) {
			Port x = link.getValue();
			Port y = links.get(x);
			if (y == null) y = x.hide();
			link.setValue(y.update(x));
		}
	}

	@Override
	public Instance evaluate(Map<VariableName, Expression> params)
			throws Exception {
		return this;
	}
}
