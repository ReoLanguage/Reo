package nl.cwi.reo.interpret;

import java.util.Map;


public class ComponentVariable implements Component {
	
	private Variable var;
	
	public ComponentVariable(Variable var) {
		if (var == null)
			throw new IllegalArgumentException("Arguments cannot be null.");
		this.var = var;
	}
	
	@Override
	public Component evaluate(Map<VariableName, Expression> params) throws Exception {
		Variable var_p = var.evaluate(params);
		if (var_p instanceof VariableName) {
			VariableName name = (VariableName)var_p;
			Expression expr = params.get(name);
			if (expr instanceof ZComponentValue) {
				return (ZComponentValue)expr;
			} else {
				throw new Exception("Variable " + name.getName() + " is not of type component.");
			}
		}
		return new ComponentVariable(var_p);		
	}

	@Override
	public Component instantiate(ExpressionList values,
			Interface iface) throws Exception {
		return this;
	}
	
	@Override
	public String toString() {
		return "" + var;
	}
}
