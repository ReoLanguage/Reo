package nl.cwi.reo.interpret;

import java.util.Map;


public class IntegerVariable implements IntegerExpression {
	
	/**
	 * Variable name.
	 */
	private Variable var;
	
	/**
	 * Constructs a natural number from a string.
	 * @param s 	string representation of a natural number
	 */
	public IntegerVariable(Variable var) {
		if (var == null)
			throw new IllegalArgumentException("Argument cannot be null.");
		this.var = var;
	}
	
	public Variable getVariable() {
		return var;
	}

	/**
	 * Evaluates this natural number to a Integer.
	 * @param params		 	parameter assignment
	 * @return Integer evaluation with respect to parameter assignment.
	 */
	@Override
	public IntegerExpression evaluate(Map<VariableName, Expression> params) throws Exception {
		Variable x = var.evaluate(params);
		if (x instanceof VariableName) {
			VariableName n = (VariableName)x;
			Expression e = params.get(n);
			if (e instanceof IntegerValue) {
				return (IntegerValue)e;
			} else {
				throw new Exception("Variable " + n + " is not of type integer.");
			}
		}
		return new IntegerVariable(x);
	}
	
	@Override
	public String toString() {
		return "" + var;
	}
}
