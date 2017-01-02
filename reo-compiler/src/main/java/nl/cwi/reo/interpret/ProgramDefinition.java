package nl.cwi.reo.interpret;


public final class ProgramDefinition implements Program {

	private final Definition def;
	
	public ProgramDefinition(Definition def) {
		if (def == null)
			throw new IllegalArgumentException("Argument cannot be null.");
		this.def = def;
	}
	
	@Override
	public Program evaluate(DefinitionList params) throws Exception {
		return new ProgramDefinition(def.evaluate(params));
	}
}
