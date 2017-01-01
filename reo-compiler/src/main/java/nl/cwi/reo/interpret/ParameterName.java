package nl.cwi.reo.interpret;


public class ParameterName implements Parameter {
	
	private String name;
	
	private ParameterType type;
	
	public ParameterName(String name, ParameterType type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return this.name;
	}
	
	public ParameterType getType() {
		return this.type;
	}

	@Override
	public Parameter evaluate(DefinitionList params)
			throws Exception {
		return this;
	}
}
