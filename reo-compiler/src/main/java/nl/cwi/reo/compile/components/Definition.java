/**
 * 
 */
package nl.cwi.reo.compile.components;

import java.util.List;

import nl.cwi.reo.interpret.ports.Port;

/**
 * A target language independent template for an executable Reo component.
 */
public interface Definition {

	public String getName();
	
	public List<Port> getInterface();
	
	public String getRun();
	
	public String getActivate();
	
	public Behavior getBehavior();
}
