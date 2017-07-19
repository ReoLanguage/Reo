package nl.cwi.reo.interpret.variables;

// TODO: Auto-generated Javadoc
/**
 * Interpretation of a parameter type.
 */
public interface ParameterType {

	/**
	 * Checks if this parameter type defines the same type as a given parameter
	 * type.
	 * 
	 * @param other
	 *            other parameter type
	 * @return true, if the other parameter type defines the same type, and
	 *         false otherwise.
	 */
	public boolean equalType(ParameterType other);

}
