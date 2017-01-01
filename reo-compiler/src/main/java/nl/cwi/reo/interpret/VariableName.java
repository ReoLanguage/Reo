package nl.cwi.reo.interpret;

public final class VariableName implements Variable {

	private final String name;
	
	public VariableName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	@Override
	public Variable evaluate(DefinitionList params) throws Exception {
		return this;
	}
}
