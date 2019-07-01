/**
 * 
 */
package nl.cwi.reo.templates;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.predicates.Term;
import nl.cwi.reo.semantics.predicates.MemoryVariable;

/**
 * Compiled automaton that is independent of the target language.
 */
public class Protocol implements Component {

	/** The protocol. */
	public final boolean protocol = true;

	/** The name. */
	private final String name;

	/** The ports. */
	private final Set<Port> ports;

	/** The transitions. */
	public final Set<Transition> transitions;

	/** The initial. */
	private final Map<MemoryVariable, Term> initial;

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
	public Protocol(String name,  Set<Transition> transitions, Map<MemoryVariable, Term> initial) {
		this.name = name;
		Set<Port> ports = new HashSet<>();
		for (Transition t : transitions)
			ports.addAll(t.getInterface());
		this.ports = ports;
		this.transitions = transitions;

		this.initial = new HashMap<>();
		for (Transition t : transitions){
			this.initial.putAll(t.getInitial());
		}
		//Overwrite initial if not null
		for (MemoryVariable mv : initial.keySet())
			this.initial.put(new MemoryVariable(mv.getName(),false,mv.getTypeTag()),initial.get(mv));
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
	public Map<MemoryVariable, Term> getInitial() {
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
