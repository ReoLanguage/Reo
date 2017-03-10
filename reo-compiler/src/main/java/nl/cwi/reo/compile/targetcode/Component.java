/**
 * 
 */
package nl.cwi.reo.compile.targetcode;

import java.util.List;

import nl.cwi.reo.interpret.ports.Port;

/**
 * A target language independent template for an executable Reo component.
 */
public interface Component {

	public String getName();
	
	public List<Port> getInterface();
	
	public String getRun();
	
	public String getActivate();
	
	public Behavior getBehavior();
	
	public String getReoFile();
}
