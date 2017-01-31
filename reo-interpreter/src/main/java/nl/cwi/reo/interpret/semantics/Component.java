package nl.cwi.reo.interpret.semantics;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import nl.cwi.reo.errors.CompilationException;
import nl.cwi.reo.semantics.api.Evaluable;
import nl.cwi.reo.semantics.api.Expression;
import nl.cwi.reo.semantics.api.Port;
import nl.cwi.reo.semantics.api.Semantics;
import nl.cwi.reo.semantics.api.SourceCode;

public final class Component<T extends Semantics<T>> extends HashMap<Port, Port> implements Evaluable<Component<T>> {
	
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
	public Component(T atom) {
		this.atom = atom;
		this.source = new SourceCode();
		for (Port a : atom.getInterface()) 
			super.put(a, a);
	}
	
	public Component(T atom, Map<Port, Port> links, SourceCode source) {
		this.atom = atom;
		this.source = source;
		super.putAll(links);
	}
	
	/**
	 * Copy constructor.
	 * @param instance
	 */
	public Component(Component<T> instance) {
		if (instance == null)
			throw new NullPointerException();
		this.atom = instance.atom;
		this.source = instance.source;
		super.putAll(new HashMap<Port, Port>(instance));
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
	@Deprecated
	public void joinAndHide(Map<Port, Port> links) {
		for (Map.Entry<Port, Port> link : this.entrySet()) {
			Port x = link.getValue();
			Port y = links.get(x);
			if (y == null) y = x.hide();
			link.setValue(y.join(x));
		}
	}
	
	public void connect(Map<Port, Port> links) {
		for (Map.Entry<Port, Port> link : this.entrySet()) {
			Port x = link.getValue();
			Port y = links.get(x);
			if (y == null) y = x;
			link.setValue(y.join(x));
		}
	}
	
	public void restrict(Collection<? extends Port> iface) {
		for (Map.Entry<Port, Port> link : this.entrySet()) 
			if (!iface.contains(link.getValue()))
				link.setValue(link.getValue().hide());
	}

	@Override
	public Component<T> evaluate(Map<String, Expression> params) throws CompilationException {
		return this;
	}
}
