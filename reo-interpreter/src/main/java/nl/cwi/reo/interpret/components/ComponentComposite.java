package nl.cwi.reo.interpret.components;

import java.util.Map;

import nl.cwi.reo.interpret.arrays.Expression;
import nl.cwi.reo.interpret.arrays.ExpressionRange;
import nl.cwi.reo.interpret.programs.ProgramExpression;
import nl.cwi.reo.interpret.programs.ProgramValue;
import nl.cwi.reo.interpret.semantics.Definitions;
import nl.cwi.reo.interpret.semantics.InstanceList;
import nl.cwi.reo.interpret.signatures.Interface;
import nl.cwi.reo.interpret.signatures.Signature;
import nl.cwi.reo.interpret.signatures.SignatureExpression;
import nl.cwi.reo.interpret.variables.VariableName;

public class ComponentComposite implements ComponentExpression {
	
	private SignatureExpression sign;
	
	private ProgramExpression body;

	public ComponentComposite(SignatureExpression sign, ProgramExpression body) {
		if (sign == null || body == null)
			throw new NullPointerException();
		this.sign = sign;
		this.body = body;
	}
	
	public ComponentExpression evaluate(Map<VariableName, Expression> params) throws Exception {
		ProgramExpression prog = body.evaluate(params);
		if (prog instanceof ProgramValue)
			return new ComponentValue(sign, (ProgramValue)prog);
		return new ComponentComposite(sign, prog);
	}

	@Override
	public ProgramExpression instantiate(ExpressionRange values, Interface iface) throws Exception {
		Signature v = sign.evaluate(values, iface);
		ProgramExpression _body = body.evaluate(v.getDefinitions());
		if (_body instanceof ProgramValue) {
			Definitions _definitions = new Definitions(((ProgramValue)_body).getUnifications());
			InstanceList _instances = new InstanceList(((ProgramValue)_body).getInstances());
			return new ProgramValue(_definitions, _instances);
		}
		return null;
	}
	
	@Override
	public String toString() {
		return "" + sign + body;
	}
}
