/**
 * 
 */
package nl.cwi.reo.compile.components;

import java.util.Collections;
import java.util.List;

import nl.cwi.reo.interpret.ports.Port;

public class Instance {
	
	private final String name;
	
	private final Definition definition;
	
	private final List<Port> ports;
	
	public Instance(String name, Definition definition, List<Port> ports) {
		this.name = name;
		this.definition = definition;
		this.ports = Collections.unmodifiableList(ports);
	}

	public String getName() {
		return name;
	}
	
	public Definition getDefinition() {
		return definition;
	}
	
	public List<Port> getPorts() {
		return ports;
	}
}
