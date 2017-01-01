package nl.cwi.reo.interpret;


public interface ComponentExpression extends Expression {

	/**
	 * Substitutes (component) variables with (component) expressions.
	 */
	public ComponentExpression evaluate(DefinitionList params) 
			throws Exception;
	
	/**
	 * 
	 * @param values
	 * @param nodes
	 * @return
	 */
	public ComponentExpression instantiate(ExpressionList values, 
			VariableList nodes) throws Exception;

}
