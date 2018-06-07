/**
 * 
 */
package nl.cwi.reo.templates.treo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.predicates.Constant;
import nl.cwi.reo.semantics.predicates.Function;
import nl.cwi.reo.semantics.predicates.MemoryVariable;
import nl.cwi.reo.semantics.predicates.Term;
import nl.cwi.reo.templates.Protocol;
import nl.cwi.reo.templates.Transition;

/**
 * Maude Protocol template
 */
public final class TreoProtocol extends Protocol {


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
	public TreoProtocol(String name, Set<Port> ports, Set<Transition> transitions, Map<MemoryVariable, Object> initial) {	
		super(name,ports,transitions,initial);
	}


	public Set<Port> getRenamedPorts() {
		Set<Port> set = new HashSet<>();
		for(Port p :  super.getPorts()){
			set.add(p.rename("p"+p.getName().substring(1)));
		}
		return set;
	}

	
}
