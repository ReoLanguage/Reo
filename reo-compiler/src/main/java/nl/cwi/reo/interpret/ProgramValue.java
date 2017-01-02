package nl.cwi.reo.interpret;

public final class ProgramValue implements Program {
	
	/**
	 * Components.
	 */
	private final Instance instances;
	
	/**
	 * Local definitions.
	 */
	private final DefinitionList definitions;

	/**
	 * Constructs an empty body of components and definitions.
	 */
	public ProgramValue() {
		this.instances = new Instance();
		this.definitions = new DefinitionList();
	}

	/**
	 * Constructs a body of components and definitions.
	 * @param components	set of component expressions
	 * @param definitions	map of definitions
	 */
	public ProgramValue(Instance component, DefinitionList definitions) {
		if (component == null || definitions == null)
			throw new IllegalArgumentException("Arguments cannot be null.");
		this.instances = component;
		this.definitions = definitions;
	}
	
	/**
	 * Gets the component defined by this body.
	 * @return defined component
	 */
	public Instance getInstances() {
		return instances;
	}
	
	/**
	 * Gets all definitions in this body
	 * @return map assigning to values to strings.
	 */
	public DefinitionList getDefinitions() {
		return definitions;
	}

	@Override
	public ProgramValue evaluate(DefinitionList params)
			throws Exception {
		return new ProgramValue(instances.evaluate(params), 
				definitions.evaluate(params));
	}

}
