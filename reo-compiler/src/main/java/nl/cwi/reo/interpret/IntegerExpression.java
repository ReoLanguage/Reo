package nl.cwi.reo.interpret;


public interface IntegerExpression extends Expression {
	
	public IntegerExpression evaluate(DefinitionList params) throws Exception;
	
}
