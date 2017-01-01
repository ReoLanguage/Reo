package nl.cwi.reo.interpret;

public class ComponentComposite implements ComponentExpression {
	
	private SignatureExpression sign;
	
	private ProgramExpression body;

	public ComponentComposite(SignatureExpression sign, ProgramExpression body) {
		this.sign = sign;
		this.body = body;
	}
	
	public ComponentExpression evaluate(DefinitionList params) throws Exception {
		return this;
	}

	@Override
	public ComponentExpression instantiate(ExpressionList values, 
			VariableList nodes) throws Exception {
		SignatureValue v = sign.evaluate(values, nodes);
		return new ComponentComposite(sign, body.evaluate(v.getParameters()));
	}
}
