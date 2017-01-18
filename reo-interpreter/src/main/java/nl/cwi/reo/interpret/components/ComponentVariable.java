package nl.cwi.reo.interpret.components;

import java.util.Map;

import nl.cwi.reo.interpret.ranges.Range;
import nl.cwi.reo.interpret.blocks.Statement;
import nl.cwi.reo.interpret.ranges.Expression;
import nl.cwi.reo.interpret.ranges.ExpressionList;
import nl.cwi.reo.interpret.variables.Variable;
import nl.cwi.reo.interpret.variables.VariableName;
import nl.cwi.reo.interpret.variables.VariableNameList;
import nl.cwi.reo.semantics.Semantics;

public class ComponentVariable<T extends Semantics<T>> implements ComponentExpression<T> {
	
	private Variable var;
	
	public ComponentVariable(Variable var) {
		if (var == null)
			throw new NullPointerException();
		this.var = var;
	}

	@Override
	public Statement<T> instantiate(ExpressionList values, VariableNameList iface) throws Exception {
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ComponentExpression<T> evaluate(Map<VariableName, Expression> params) throws Exception {
		Range e = var.evaluate(params);
		if (e instanceof ComponentExpression) {
			return (ComponentExpression<T>)e;
		} else if (e instanceof Variable) {
			return new ComponentVariable<T>((Variable)e);
		} 
		return this;	
	}
	
	@Override
	public String toString() {
		return "" + var;
	}
}
