package nl.cwi.reo.interpret;

public interface Variable extends Expression {
	
	public Variable evaluate(DefinitionList params) throws Exception;

}
