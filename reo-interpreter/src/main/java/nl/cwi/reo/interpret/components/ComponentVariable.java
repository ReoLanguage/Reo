package nl.cwi.reo.interpret.components;

import java.util.Map;

import nl.cwi.reo.interpret.arrays.Array;
import nl.cwi.reo.interpret.arrays.Expression;
import nl.cwi.reo.interpret.arrays.ExpressionRange;
import nl.cwi.reo.interpret.programs.ProgramExpression;
import nl.cwi.reo.interpret.signatures.Interface;
import nl.cwi.reo.interpret.variables.Variable;
import nl.cwi.reo.interpret.variables.VariableName;

public class ComponentVariable implements ComponentExpression {
	
	private Variable var;
	
	public ComponentVariable(Variable var) {
		if (var == null)
			throw new NullPointerException();
		this.var = var;
	}

	@Override
	public ProgramExpression instantiate(ExpressionRange values, Interface iface) throws Exception {
		return null;
	}
	@Override
	public ComponentExpression evaluate(Map<VariableName, Expression> params) throws Exception {
		Array e = var.evaluate(params);
		if (e instanceof ComponentExpression) {
			return (ComponentExpression)e;
		} else if (e instanceof Variable) {
			return new ComponentVariable((Variable)e);
		} 
		return this;	
	}
	
	@Override
	public String toString() {
		return "" + var;
	}
}
