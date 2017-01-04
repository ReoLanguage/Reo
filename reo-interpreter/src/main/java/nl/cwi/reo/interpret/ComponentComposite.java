package nl.cwi.reo.interpret;

import java.util.Map;

public class ComponentComposite implements Component {
	
	private Signature sign;
	
	private ProgramExpression body;

	public ComponentComposite(Signature sign, ProgramExpression body) {
		if (sign == null || body == null)
			throw new IllegalArgumentException("Arguments cannot be null.");
		this.sign = sign;
		this.body = body;
	}
	
	public Component evaluate(Map<VariableName, Expression> params) throws Exception {
		ProgramExpression prog = body.evaluate(params);
		if (prog instanceof Program)
			return new ZComponentValue(sign, ((Program)prog).getInstance());
		return new ComponentComposite(sign, prog);
	}

	@Override
	public Component instantiate(ExpressionList values, 
			Interface iface) throws Exception {
		SignatureInstance v = sign.evaluate(values, iface);
		ProgramExpression prog = body.evaluate(v.getDefinitions());
		if (prog instanceof Program)
			return new ZComponentValue(sign, ((Program)prog).getInstance());
		return new ComponentComposite(sign, prog);
	}
	
	@Override
	public String toString() {
		return sign + "{" + body + "}";
	}
}
