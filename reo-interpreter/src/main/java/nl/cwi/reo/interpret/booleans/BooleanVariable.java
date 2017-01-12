package nl.cwi.reo.interpret.booleans;

import java.util.Map;

import nl.cwi.reo.interpret.arrays.Array;
import nl.cwi.reo.interpret.arrays.Expression;
import nl.cwi.reo.interpret.variables.Variable;
import nl.cwi.reo.interpret.variables.VariableName;


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
			throw new NullPointerException();
		this.var = var;
	}
	
	/**
	 * Evaluates this natural number to a Integer.
	 * @param params		 	parameter assignment
	 * @return Integer evaluation with respect to parameter assignment.
	 */
	@Override
	public BooleanExpression evaluate(Map<VariableName, Expression> params) throws Exception {
		Array e = var.evaluate(params);
		if (e instanceof BooleanExpression) {
			return (BooleanExpression)e;
		} else if (e instanceof Variable) {
			return new BooleanVariable((Variable)e);
		} 
		return this;
	}
	
	@Override
	public String toString() {
		return "" + var;
	}
}
