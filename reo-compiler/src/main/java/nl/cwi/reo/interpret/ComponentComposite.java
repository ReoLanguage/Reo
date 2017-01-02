package nl.cwi.reo.interpret;

public class ComponentComposite implements ComponentExpression {
	
	private Signature sign;
	
	private Program body;

	public ComponentComposite(Signature sign, Program body) {
		if (sign == null || body == null)
			throw new IllegalArgumentException("Arguments cannot be null.");
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
