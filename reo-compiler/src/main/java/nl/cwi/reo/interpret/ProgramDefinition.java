package nl.cwi.reo.interpret;


public final class ProgramDefinition implements ProgramExpression {

	private final DefinitionExpression def;
	
	public ProgramDefinition(DefinitionExpression def) {
		this.def = def;
	}
	
	@Override
	public ProgramExpression evaluate(DefinitionList params) throws Exception {
		return new ProgramDefinition(def.evaluate(params));
	}
}
