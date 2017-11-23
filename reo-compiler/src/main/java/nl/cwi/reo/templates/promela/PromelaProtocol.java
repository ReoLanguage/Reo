/**
 * 
 */
package nl.cwi.reo.templates.promela;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.predicates.MemoryVariable;
import nl.cwi.reo.templates.Component;
import nl.cwi.reo.templates.Transition;

/**
 * Compiled automaton that is independent of the target language.
 */
public class PromelaProtocol implements Component {

	/*
	 * Rule to name ports in template :
	 * 	- $4 is renamed as p4 for ports and m4 for mem_cells
	 */
	
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
	public PromelaProtocol(String name, Set<Port> ports, Set<Transition> transitions, Map<MemoryVariable, Object> initial) {
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
		Set<Port> setPorts = new HashSet<>();
		for(Port p : ports){
			setPorts.add(p.rename("p"+p.getName().substring(1)));
		}
		return setPorts;
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
