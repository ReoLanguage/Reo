package nl.cwi.reo.interpret;

import java.util.HashMap;
import java.util.Map;

import nl.cwi.reo.interpret.values.Value;
import nl.cwi.reo.interpret.variables.Identifier;

/**
 * A finite set of assignments.
 */
public final class Scope extends HashMap<Identifier, Value> {

	/**
	 * Serial version ID
	 */
	private static final long serialVersionUID = 3999038009879620308L;

	/**
	 * Constructs an empty scope.
	 */
	public Scope() {
	}
	
	/**
	 * Constructs a new scope.
	 * @param s		set of assignments
	 */
	public Scope(Map<Identifier, Value> s) {
		super.putAll(s);
	}

	/**
	 * Extends this scope with a new key value pair, or returns this
	 * scope if this key has already a value.
	 * @param key		identifier
	 * @param value		value
	 * @return new scope that contains all key value pairs in this scope
	 * plus the given key value pair, if this scope does not contains the
	 * new given key.
	 */
	public Scope extend(Identifier key, Value value) {
		Map<Identifier, Value> s = new HashMap<Identifier, Value>(this);
		if (!s.containsKey(key))
			s.put(key, value);
		else
			return this;
		return new Scope(s);
	}
}
