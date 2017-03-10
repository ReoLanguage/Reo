/**
 * 
 */
package nl.cwi.reo.semantics.imperativeautomata;

/**
 * @author dokter
 *
 */
public final class Assignment {
	
	private final String var;
	
	private final String value;
	
	public Assignment(String var, String value){
		this.var = var;
		this.value = value;
	}

	public String getVariable() {
		return var;
	}
	
	public String getValue() {
		return value;
	}
}
