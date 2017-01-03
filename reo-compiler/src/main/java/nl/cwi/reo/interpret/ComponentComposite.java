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
		Program prog = body.evaluate(params);
		if (prog instanceof ProgramValue)
			return new ComponentValue(sign, ((ProgramValue)prog).getInstance());
		return new ComponentComposite(sign, prog);
	}

	@Override
	public ComponentExpression instantiate(ExpressionList values, 
			Interface iface) throws Exception {
		SignatureInstance v = sign.evaluate(values, iface);
		Program prog = body.evaluate(v.getDefinitions());
		if (prog instanceof ProgramValue)
			return new ComponentValue(sign, ((ProgramValue)prog).getInstance());
		return new ComponentComposite(sign, prog);
	}
}
