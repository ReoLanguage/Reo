package nl.cwi.reo.interpret;

import java.util.Map;


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
		if (var == null)
			throw new IllegalArgumentException("Argument cannot be null.");
		this.var = var;
	}
	
	/**
	 * Evaluates this natural number to a Integer.
	 * @param params		 	parameter assignment
	 * @return Integer evaluation with respect to parameter assignment.
	 */
	@Override
	public BooleanExpression evaluate(Map<VariableName, Expression> params) throws Exception {
		Variable x = var.evaluate(params);
		if (x instanceof VariableName) {
			VariableName n = (VariableName)x;
			Expression e = params.get(n);
			if (e == null || e instanceof BooleanExpression) {
				if (e instanceof BooleanValue)
					return (BooleanValue)e;
			} else {
				throw new Exception("Variable " + n + " is not of type boolean.");
			}
		}
		return new BooleanVariable(x);
	}
	
	@Override
	public String toString() {
		return "" + var;
	}
}
