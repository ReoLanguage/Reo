package nl.cwi.reo.interpret;

import java.util.Map;

public class ComponentVariable implements ComponentExpression {
	
	private Variable var;
	
	public ComponentVariable(Variable var) {
		if (var == null)
			throw new IllegalArgumentException("Arguments cannot be null.");
		this.var = var;
	}
	
	@Override
	public ComponentExpression evaluate(Map<VariableName, Expression> params) throws Exception {
		Variable var_p = var.evaluate(params);
		if (var_p instanceof VariableName) {
			VariableName name = (VariableName)var_p;
			Expression e = params.get(name);
			if (e == null || e instanceof ComponentExpression) {
				if (e instanceof ComponentValue)
					return (ComponentValue)e;
			} else {
				System.out.println("value of variable : " + e);
				throw new Exception("Variable " + name.getName() + " is not of type component.");
			}
		}
		return new ComponentVariable(var_p);		
	}
	
	@Override
	public String toString() {
		return "" + var;
	}
}
