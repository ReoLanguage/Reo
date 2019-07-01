/**
 * 
 */
package nl.cwi.reo.templates.treo;

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
public final class TreoAtomic extends Atomic {


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
	public TreoAtomic(String name, List<Value> params, Set<Port> ports, String call) {
		super(name,params,ports,call);
	}
	
	@Override
	public Set<Port> getPorts() {
		Set<Port> ports = new HashSet<>();
		for(Port p : super.getPorts()) {
			if(p.getName().substring(0, 1).contains("$")) {
				ports.add(p.rename("p"+p.getName().substring(1,p.getName().length())));
			}
			else
				ports.add(p);
		}
		return ports;
	}
}
