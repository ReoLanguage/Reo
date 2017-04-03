/**
 * 
 */
package nl.cwi.reo.compile.components;

import java.util.Map;
import java.util.Set;

import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.predicates.MemoryCell;

/**
 * Compiled automaton that is independent of the target language.
 */
public final class Protocol implements Component {
	
	public final boolean automaton = true;

	private final String name;

	private final Set<Port> ports;
	
	private final Set<MemoryCell> mem;

	public final Set<Transition> transitions;

	private final Map<MemoryCell, Object> initial;

	public Protocol(String name, Set<Port> ports, Set<Transition> transitions, Set<MemoryCell> mem, Map<MemoryCell, Object> initial) {
		this.name = name;
		this.ports = ports;
		this.transitions = transitions;
		this.mem = mem;
		this.initial = initial;
	}

	public Set<Transition> getTransitions() {
		return transitions;
	}

	public Map<MemoryCell, Object> getInitial() {
		return initial;
	}

	public String getName() {
		return name;
	}

	public Set<Port> getPorts() {
		return ports;
	}
	public Set<MemoryCell> getMem() {
		return mem;
	}
}
