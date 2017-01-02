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
	 * @param iface
	 * @return
	 */
	public ComponentExpression instantiate(ExpressionList values, 
			Interface iface) throws Exception;

}
