/**
 * 
 */
package nl.cwi.reo.templates.treo;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.predicates.MemoryVariable;
import nl.cwi.reo.templates.Protocol;
import nl.cwi.reo.templates.Transition;

/**
 * Maude Protocol template
 */
public final class TreoProtocol extends Protocol{
	private final Set<Port> renamedPorts;

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
		this.renamedPorts = new HashSet<>();
		for(Port p : ports) {
			if(p.getName().substring(0, 1).contains("$")) {
				renamedPorts.add(p.rename("p"+p.getName().substring(1,p.getName().length())));
			}
			else
				renamedPorts.add(p);
		}
	}

	public Set<Port> getRenamedPorts() {
		return renamedPorts;
	}
	
}
