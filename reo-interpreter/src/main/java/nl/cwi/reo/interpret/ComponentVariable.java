package nl.cwi.reo.interpret;

import java.util.Map;

public class ComponentVariable implements ComponentExpression {
	
	private Variable var;
	
	public ComponentVariable(Variable var) {
		if (var == null)
			throw new NullPointerException();
		this.var = var;
	}
	
	@Override
	public ComponentExpression evaluate(Map<VariableName, Expression> params) throws Exception {
		Expression e = var.evaluate(params);
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
