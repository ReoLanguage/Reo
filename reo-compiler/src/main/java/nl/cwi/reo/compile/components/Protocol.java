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

	public final Set<Transition> transitions;

	private final Map<String, Object> initial;

	public Protocol(String name, Set<Port> ports, Set<Transition> transitions, Map<String, Object> initial) {
		this.name = name;
		this.ports = ports;
		this.transitions = transitions;
		this.initial = initial;
	}

	public Set<Transition> getTransitions() {
		return transitions;
	}

	public Map<String, Object> getInitial() {
		return initial;
	}

	public String getName() {
		return name;
	}

	public Set<Port> getPorts() {
		return ports;
	}
}
