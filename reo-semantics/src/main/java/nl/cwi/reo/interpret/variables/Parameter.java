package nl.cwi.reo.interpret.variables;

import nl.cwi.reo.interpret.typetags.TypeTag;

/**
 * An identifier decorated with a parameter type.
 */
public final class Parameter extends Identifier {
	
	/**
	 * Type of this parameter.
	 */
	private final ParameterType type;

	/**
	 * Constructs a new parameter.
	 * @param name		parameter name
	 * @param type		parameter type
	 */
	public Parameter(String name, ParameterType type) {
		super(name);
		this.type = type;
	}

	/**
	 * Gets the type of this parameter.
	 * @return	parameter type
	 */
	public ParameterType getType() {
		return type;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
	    return type instanceof TypeTag ? name + ":" + type : name;
	}
}
