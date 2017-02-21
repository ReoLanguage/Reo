package nl.cwi.reo.interpret.variables;

import nl.cwi.reo.interpret.terms.Term;

/**
 * A concatenation of a fully qualified name and 
 * a sequence of indices.
 */
public class Identifier implements Variable {

	/**
	 * Name.
	 */
	protected final String name;
	
	/**
	 * Constructs a new identifier.
	 * @param name		identifier name
	 */
	public Identifier(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the name of this identifier.
	 * @return	identifier name
	 */
	public String getName() {
		return name;
	}
}
