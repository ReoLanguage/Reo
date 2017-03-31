/**
 * 
 */
package nl.cwi.reo.compile.components;

import java.util.Set;

import nl.cwi.reo.interpret.ports.Port;

/**
 * A target language independent template for an executable Reo component.
 */
public interface Component {
	
	public Set<Port> getPorts();
	
}
