/**
 * 
 */
package nl.cwi.reo.templates.maude;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.values.Value;
import nl.cwi.reo.templates.Atomic;

/**
 * Compiled atomic component that is independent of the target language.
 */
public final class MaudeAtomic extends Atomic {

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
	public MaudeAtomic(String name, List<Value> params, Set<Port> ports, String call) {
		super(name, params, ports, call);
	}

	public String getAtomicName(){
		return getName().substring(0, getName().length()-1);
	}

	public Set<Port> getRenamedPorts(){
		LinkedHashSet<Port> l = new LinkedHashSet<>();
		for(Port p : getPorts())
			l.add(p.rename("p"+p.getName()));
		return l;
	}
	
}
