/**
 * 
 */
package nl.cwi.reo.compile.components;

import java.util.List;

import nl.cwi.reo.interpret.ports.Port;

/**
 * Compiled atomic component that is independent of the target language.
 */
public final class AtomicComponent implements Definition {
	
	public final boolean component = true;

	private final String name;

	private final List<Port> ports;

	public AtomicComponent(String name, List<Port> ports) {
		this.name = name;
		this.ports = ports;
	}

	public String getName() {
		return name;
	}

	public List<Port> getInterface() {
		return ports;
	}
}
