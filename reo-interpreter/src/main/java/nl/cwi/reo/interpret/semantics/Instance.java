package nl.cwi.reo.interpret.semantics;

import java.util.HashMap;
import java.util.Map;

import nl.cwi.reo.interpret.Evaluable;
import nl.cwi.reo.interpret.ranges.Expression;
import nl.cwi.reo.interpret.variables.VariableName;
import nl.cwi.reo.semantics.Port;
import nl.cwi.reo.semantics.Semantics;


public final class Instance<T extends Semantics<T>> extends HashMap<String, Port> implements Evaluable<Instance<T>> {
	
	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 6594525018732378080L;
	
	private final T atom;
	
	private final SourceCode source;
	
	/**
	 * Constructs a new instance from an atom.
	 * @param atom		semantics object
	 */
	public Instance(T atom) {
		this.atom = atom;
		this.source = new SourceCode();
		for (String a : atom.getInterface()) 
			super.put(a, new Port(a));
	}
	
	public Instance(T atom, Map<String, Port> links, SourceCode source) {
		this.atom = atom;
		this.source = source;
		super.putAll(links);
	}
	
	/**
	 * Copy constructor.
	 * @param instance
	 */
	public Instance(Instance<T> instance) {
		if (instance == null)
			throw new NullPointerException();
		this.atom = instance.atom;
		this.source = instance.source;
		super.putAll(new HashMap<String, Port>(instance));
	}
	
	public T getAtom() {
		return atom;
	}
	
	public SourceCode getSourceCode() {
		return source;
	}
	
	/**
	 * Renames the external ports, and hides all internal ports
	 * @param links		maps external ports to new ports.
	 */
	public void joinAndHide(Map<Port, Port> links) {
		for (Map.Entry<String, Port> link : this.entrySet()) {
			Port x = link.getValue();
			Port y = links.get(x);
			if (y == null) y = x.hide();
			link.setValue(y.join(x));
		}
	}

	@Override
	public Instance<T> evaluate(Map<VariableName, Expression> params)
			throws Exception {
		return this;
	}
}
