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
		Variable x = var.evaluate(params);
		if (x instanceof VariableName) {
			VariableName n = (VariableName)x;
			Expression e = params.get(n);
			if (e instanceof ComponentValue) {
				return (ComponentValue)e;
			} else {
				throw new Exception("Variable " + n + " is not of type boolean.");
			}
		}
		return new ComponentVariable(x);		
	}

	@Override
	public ComponentExpression instantiate(ExpressionList values,
			Interface iface) throws Exception {
		return this;
	}
}
