package nl.cwi.reo.interpret;

import java.util.Map;

public class ComponentComposite implements ComponentExpression {
	
	private Signature sign;
	
	private ProgramExpression body;

	public ComponentComposite(Signature sign, ProgramExpression body) {
		if (sign == null || body == null)
			throw new IllegalArgumentException("Arguments cannot be null.");
		this.sign = sign;
		this.body = body;
	}
	
	public ComponentExpression evaluate(Map<VariableName, Expression> params) throws Exception {
		ProgramExpression prog = body.evaluate(params);
		if (prog instanceof ProgramValue)
			return (ProgramValue)prog;
		return new ComponentComposite(sign, prog);
	}
	
	@Override
	public String toString() {
		return sign + "{" + body + "}";
	}
}
