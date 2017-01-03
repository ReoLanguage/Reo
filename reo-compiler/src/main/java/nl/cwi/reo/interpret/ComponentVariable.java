package nl.cwi.reo.interpret;


public class ComponentVariable implements ComponentExpression {
	
	private Variable var;
	
	public ComponentVariable(Variable var) {
		if (var == null)
			throw new IllegalArgumentException("Arguments cannot be null.");
		this.var = var;
	}
	
	@Override
	public ComponentExpression evaluate(DefinitionList params) throws Exception {
		Variable var_p = var.evaluate(params);
		if (var_p instanceof VariableName) {
			VariableName name = (VariableName)var_p;
			Expression expr = params.get(name);
			if (expr instanceof ComponentValue) {
				return (ComponentValue)expr;
			} else {
				throw new Exception("Variable " + name + " is not of type component.");
			}
		}
		return new ComponentVariable(var_p);		
	}

	@Override
	public ComponentExpression instantiate(ExpressionList values,
			Interface iface) throws Exception {
		return this;
	}
}
