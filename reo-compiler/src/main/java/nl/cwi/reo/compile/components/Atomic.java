/**
 * 
 */
package nl.cwi.reo.compile.components;

import java.util.List;
import java.util.Set;

import nl.cwi.reo.interpret.ports.Port;

/**
 * Compiled atomic component that is independent of the target language.
 */
public final class Atomic implements Component {
	
	/**
	 * Flag for string template.
	 */
	public final boolean atomic = true;

	private final String name;
	
	private final List<String> params;

	private final Set<Port> ports;
	
	private final String call;

	public Atomic(String name, List<String> params, Set<Port> ports, String call) {
		this.name = name;
		this.params = params;
		this.ports = ports;
		this.call = call;
	}

	public String getName() {
		return name;
	}
	
	public List<String> getParameters() {
		return params;
	}

	public Set<Port> getPorts() {
		return ports;
	}
	
	public String getCall() {
		return call;
	}
}
