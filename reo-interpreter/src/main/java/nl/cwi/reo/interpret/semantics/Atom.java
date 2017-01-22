package nl.cwi.reo.interpret.semantics;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nl.cwi.reo.errors.CompilationException;
import nl.cwi.reo.interpret.ranges.Expression;
import nl.cwi.reo.interpret.strings.StringValue;
import nl.cwi.reo.interpret.variables.VariableName;
import nl.cwi.reo.semantics.Semantics;

public final class Atom<T extends Semantics<T>> implements Component1<T> {
	
	private final T semantics;
	
	private final SourceCode source;
	
	private final Set<Port<T>> iface;
	
	/**
	 * Constructs a new atomic component.
	 * @param atom		semantics
	 * @param source	reference to source code
	 */
	public Atom(T atom, SourceCode source) {
		this.semantics = atom;
		this.source = source;
		Set<Port<T>> iface = new HashSet<Port<T>>();
		for (nl.cwi.reo.semantics.Port p : semantics.getInterface()) 
			iface.add(new Port<T>(p.getName(), this));
		this.iface = Collections.unmodifiableSet(iface);
	}
	
	public T getSemantics() {
		return semantics;
	}
	
	public SourceCode getSourceCode() {
		return source;
	}

	@Override
	public Atom<T> evaluate(Map<VariableName, Expression> params) throws CompilationException {
		Map<String, String> p = new HashMap<String, String>();
		for (Map.Entry<VariableName, Expression> def : params.entrySet())
			if (def.getValue() instanceof StringValue)
				p.put(def.getKey().getName(), ((StringValue)def.getValue()).toString());
		return new Atom<T>(semantics.evaluate(p), source);
	}

	@Override
	public Set<Port<T>> getInterface() {
		return iface;
	}
}
