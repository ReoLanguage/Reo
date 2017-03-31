/**
 * 
 */
package nl.cwi.reo.compile.components;

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

	private final Set<Port> ports;
	
	private final String call;

	public Atomic(String name, Set<Port> ports, String call) {
		this.name = name;
		this.ports = ports;
		this.call = call;
	}

	public String getName() {
		return name;
	}

	public Set<Port> getPorts() {
		return ports;
	}
	
	public String getCall() {
		return call;
	}
}
