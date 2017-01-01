package nl.cwi.reo.interpret;


public class BooleanVariable implements BooleanExpression {

	/**
	 * Variable name.
	 */
	private Variable var;

	/**
	 * Constructs a natural number from a string.
	 * @param s 	string representation of a natural number
	 */
	public BooleanVariable(Variable var) {
		this.var = var;
	}
	
	/**
	 * Evaluates this natural number to a Integer.
	 * @param params		 	parameter assignment
	 * @return Integer evaluation with respect to parameter assignment.
	 */
	@Override
	public BooleanExpression evaluate(DefinitionList params) throws Exception {
		Variable x = var.evaluate(params);
		if (x instanceof VariableName) {
			VariableName n = (VariableName)x;
			Expression e = params.get(n);
			if (e instanceof BooleanValue) {
				return (BooleanValue)e;
			} else {
				throw new Exception("Variable " + n + " is not of type boolean.");
			}
		}
		return new BooleanVariable(x);
	}
}
