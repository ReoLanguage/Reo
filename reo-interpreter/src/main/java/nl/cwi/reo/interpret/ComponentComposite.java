package nl.cwi.reo.interpret;

import java.util.Map;

public class ComponentComposite implements ComponentExpression {
	
	private Signature sign;
	
	private BodyExpression body;

	public ComponentComposite(Signature sign, BodyExpression body) {
		if (sign == null || body == null)
			throw new IllegalArgumentException("Arguments cannot be null.");
		this.sign = sign;
		this.body = body;
	}
	
	public ComponentExpression evaluate(Map<VariableName, Expression> params) throws Exception {
		BodyExpression prog = body.evaluate(params);
		if (prog instanceof BodyValue)
			return new ComponentValue(sign, ((BodyValue)prog).getInstance());
		return new ComponentComposite(sign, prog);
	}

	@Override
	public ComponentExpression instantiate(ExpressionList values, 
			Interface iface) throws Exception {
		SignatureInstance v = sign.evaluate(values, iface);
		BodyExpression prog = body.evaluate(v.getDefinitions());
		if (prog instanceof BodyValue)
			return new ComponentValue(sign, ((BodyValue)prog).getInstance());
		return new ComponentComposite(sign, prog);
	}
	
	@Override
	public String toString() {
		return sign + "{" + body + "}";
	}
}
