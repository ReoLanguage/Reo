/**
 * 
 */
package nl.cwi.reo.templates;

import java.util.Set;

import nl.cwi.reo.interpret.ports.Port;

// TODO: Auto-generated Javadoc
/**
 * A target language independent template for an executable Reo component.
 */
public interface Component {

	/**
	 * Gets the ports.
	 *
	 * @return the ports
	 */
	public Set<Port> getPorts();

}
