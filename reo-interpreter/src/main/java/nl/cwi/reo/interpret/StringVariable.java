package nl.cwi.reo.interpret;

import java.util.Map;

public class StringVariable implements StringExpression {

	/**
	 * Variable name.
	 */
	private Variable var;

	/**
	 * Constructs a natural number from a string.
	 * @param s 	string representation of a natural number
	 */
	public StringVariable(Variable var) {
		if (var == null)
			throw new NullPointerException();
		this.var = var;
	}
	
	/**
	 * Evaluates this natural number to a Integer.
	 * @param params		 	parameter assignment
	 * @return Integer evaluation with respect to parameter assignment.
	 */
	@Override
	public StringExpression evaluate(Map<VariableName, Expression> params) throws Exception {
		Variable x = var.evaluate(params);
		if (x instanceof VariableName) {
			VariableName n = (VariableName)x;
			Expression e = params.get(n);
			if (e instanceof StringExpression) 
				return (StringExpression)e;
		}
		return new StringVariable(x);
	}
	
	@Override
	public String toString() {
		return "" + var;
	}
}
