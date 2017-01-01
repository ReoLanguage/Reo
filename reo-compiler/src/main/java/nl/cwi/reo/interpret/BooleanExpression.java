package nl.cwi.reo.interpret;


public interface BooleanExpression extends Expression {

	public BooleanExpression evaluate(DefinitionList params) throws Exception;
	
}
