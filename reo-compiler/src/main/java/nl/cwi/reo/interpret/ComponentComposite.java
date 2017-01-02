package nl.cwi.reo.interpret;

public class ComponentComposite implements ComponentExpression {
	
	private Signature sign;
	
	private ProgramExpression body;

	public ComponentComposite(Signature sign, ProgramExpression body) {
		this.sign = sign;
		this.body = body;
	}
	
	public ComponentExpression evaluate(DefinitionList params) throws Exception {
		return new ComponentComposite(sign, body.evaluate(params));
	}

	@Override
	public ComponentExpression instantiate(ExpressionList values, 
			Interface iface) throws Exception {
		SignatureInstance v = sign.evaluate(values, iface);
		return new ComponentComposite(sign, body.evaluate(v.getDefinitions()));
	}
}
