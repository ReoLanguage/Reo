package nl.cwi.reo.interpret;


public interface ProgramExpression extends Expression {
	
	public ProgramExpression evaluate(DefinitionList params) throws Exception;

}
