/**
 * 
 */
package nl.cwi.reo.templates;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.predicates.MemoryVariable;

// TODO: Auto-generated Javadoc
/**
 * Compiled automaton that is independent of the target language.
 */
public final class Protocol implements Component {

	/** The protocol. */
	public final boolean protocol = true;

	/** The name. */
	private final String name;

	/** The ports. */
	private final Set<Port> ports;

	/** The transitions. */
	public final Set<Transition> transitions;

	/** The initial. */
	private final Map<MemoryVariable, Object> initial;

	/** The state. */
	private final Set<String> state = new HashSet<>();

	/** The list port. */
	private final Map<Port, Integer> listPort = new HashMap<>();

	/**
	 * Instantiates a new protocol.
	 *
	 * @param name
	 *            the name
	 * @param ports
	 *            the ports
	 * @param transitions
	 *            the transitions
	 * @param initial
	 *            the initial
	 */
	public Protocol(String name, Set<Port> ports, Set<Transition> transitions, Map<MemoryVariable, Object> initial) {
		this.name = name;
		this.ports = ports;
		this.transitions = transitions;
		this.initial = initial;
	}

	/**
	 * Gets the transitions.
	 *
	 * @return the transitions
	 */
	public Set<Transition> getTransitions() {
		return transitions;
	}

	/**
	 * Gets the initial.
	 *
	 * @return the initial
	 */
	public Map<MemoryVariable, Object> getInitial() {
		return initial;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the port in the interface of this protocol.
	 *
	 * @return the port
	 */
	public Set<Port> getPorts() {
		return ports;
	}

	/**
	 * Gets the list port.
	 *
	 * @return the list port
	 */
	public Map<Port, Integer> getListPort() {
		int i = 0;
		for (Port p : ports) {
			listPort.put(p, i);
			i++;
		}
		return listPort;
	}

	/**
	 * Gets the mem.
	 *
	 * @return the mem
	 */
	public Set<MemoryVariable> getMem() {
		return initial.keySet();
	}

	/**
	 * Gets the state.
	 *
	 * @return the state
	 */
	public Set<String> getState() {
		String s = "";
		for (MemoryVariable m : initial.keySet()) {
			if (initial.get(m) != null)
				s = s + "m(" + m.getName().substring(1) + "," + initial.get(m).toString() + ") ";
			else
				s = s + "m(" + m.getName().substring(1) + "," + "*) ";
		}
		s = s + "\n";
		for (Port p : ports) {
			if (p.isInput()) {
				s = s + "out(" + p.getName().substring(1) + ")" + " p(" + p.getName().substring(1) + ",*) \n";
			} else {
				s = s + "in(" + p.getName().substring(1) + ")" + " p(" + p.getName().substring(1) + ",*) \n";
			}
			s = s + "link(" + p.getName().substring(1) + "," + p.getName().substring(1) + ") q("
					+ p.getName().substring(1) + ",0,*) \n";

		}
		state.add(s);
		return state;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String s = "protocol: " + name + ports + "\n";
		for (Transition t : transitions)
			s += "\t" + t.toString() + "\n";
		return s;
	}
}
