package nl.cwi.reo.interpret;

public interface Expression extends Value {
	
	public Expression evaluate(DefinitionList params) throws Exception;

}
