package nl.cwi.reo.interpret;


public interface Program extends Expression {
	
	public Program evaluate(DefinitionList params) throws Exception;

}
