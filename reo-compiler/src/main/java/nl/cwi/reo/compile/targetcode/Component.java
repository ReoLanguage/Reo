/**
 * 
 */
package nl.cwi.reo.compile.targetcode;

/**
 * A target language independent template for an executable Reo component.
 */
public final class Component {

	public final String name;
	
	public final Behavior type;
	
	public Component(String name, Behavior type) {
		this.name = name;
		this.type = type;
	}
}
