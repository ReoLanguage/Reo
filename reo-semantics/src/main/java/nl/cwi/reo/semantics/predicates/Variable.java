package nl.cwi.reo.semantics.predicates;

// TODO: Auto-generated Javadoc
/**
 * A variable used for the language of constraints. Possible types of variable
 * include {@link nl.cwi.reo.semantics.predicates.PortVariable} and
 * {@link nl.cwi.reo.semantics.predicates.MemoryVariable}.
 */
public interface Variable extends Term {

	/**
	 * Gets the variable name.
	 * 
	 * @return name of the variable.
	 */
	public String getName();

}
