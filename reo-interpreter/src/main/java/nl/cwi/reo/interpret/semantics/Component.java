package nl.cwi.reo.interpret.semantics;

import java.util.HashMap;
import java.util.Map;

import jdk.nashorn.internal.codegen.CompilationException;
import nl.cwi.reo.interpret.Evaluable;
import nl.cwi.reo.interpret.ranges.Expression;
import nl.cwi.reo.interpret.variables.VariableName;
import nl.cwi.reo.semantics.Port;
import nl.cwi.reo.semantics.Semantics;


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
	public void joinAndHide(Map<Port, Port> links) {
		for (Map.Entry<Port, Port> link : this.entrySet()) {
			Port x = link.getValue();
			Port y = links.get(x);
			if (y == null) y = x.hide();
			link.setValue(y.join(x));
		}
	}

	@Override
	public Component<T> evaluate(Map<VariableName, Expression> params) throws CompilationException {
		return this;
	}
}
