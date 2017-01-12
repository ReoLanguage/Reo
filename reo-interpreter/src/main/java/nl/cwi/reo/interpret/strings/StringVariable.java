package nl.cwi.reo.interpret.strings;

import java.util.Map;

import nl.cwi.reo.interpret.arrays.Array;
import nl.cwi.reo.interpret.arrays.Expression;
import nl.cwi.reo.interpret.variables.Variable;
import nl.cwi.reo.interpret.variables.VariableName;

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
		Array e = var.evaluate(params);
		if (e instanceof StringExpression) {
			return (StringExpression)e;
		} else if (e instanceof Variable) {
			return new StringVariable((Variable)e);
		} 
		return this;
	}
	
	@Override
	public String toString() {
		return "" + var;
	}
}
