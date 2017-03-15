/**
 * 
 */
package nl.cwi.reo.compile.components;

import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.cwi.reo.interpret.ports.Port;

/**
 * Compiled automaton that is independent of the target language.
 */
public final class ActiveAutomaton implements Definition {
	
	public final boolean automaton = true;

	private final String name;

	private final List<Port> ports;

	public final Map<Integer, Set<Transition>> out;

	private final Integer initial;

	public ActiveAutomaton(String name, List<Port> ports, Map<Integer, Set<Transition>> out, int initial) {
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

	public List<Port> getInterface() {
		return ports;
	}
}
