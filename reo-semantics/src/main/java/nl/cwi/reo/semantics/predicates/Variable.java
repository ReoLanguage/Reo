package nl.cwi.reo.semantics.predicates;

/**
 * A variable used for the language of constraints. Possible types of variable
 * include {@link PortVariables} and {@link MemoryVariables}.
 */
public interface Variable extends Term {

	/**
	 * Gets the variable name.
	 * 
	 * @return name of the variable.
	 */
	public String getName();

}
