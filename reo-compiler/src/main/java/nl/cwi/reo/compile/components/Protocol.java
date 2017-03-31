/**
 * 
 */
package nl.cwi.reo.compile.components;

import java.util.Map;
import java.util.Set;

import nl.cwi.reo.interpret.ports.Port;

/**
 * Compiled automaton that is independent of the target language.
 */
public final class Protocol implements Component {
	
	public final boolean automaton = true;

	private final String name;

	private final Set<Port> ports;

	public final Map<Integer, Set<Transition>> out;

	private final Integer initial;

	public Protocol(String name, Set<Port> ports, Map<Integer, Set<Transition>> out, int initial) {
		this.name = name;
		this.ports = ports;
		this.out = out;
		this.initial = initial;
	}

	public Map<Integer, Set<Transition>> getTransitions() {
		return out;
	}

	public String getInitial() {
		return initial.toString();
	}

	public String getName() {
		return name;
	}

	public Set<Port> getPorts() {
		return ports;
	}
}
