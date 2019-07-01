/**
 * 
 */
package nl.cwi.reo.templates.promela;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.values.Value;
import nl.cwi.reo.templates.Atomic;

// TODO: Auto-generated Javadoc
/**
 * Compiled atomic component that is independent of the target language.
 */
public final class PromelaAtomic extends Atomic {
	
	private final Set<Port> renamedPorts;

	/**
	 * Instantiates a new atomic.
	 *
	 * @param name
	 *            the name
	 * @param params
	 *            the params
	 * @param ports
	 *            the ports
	 * @param call
	 *            the call
	 */
	public PromelaAtomic(String name, List<Value> params, Set<Port> ports, String call) {
		super(name,params,ports,call);
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
