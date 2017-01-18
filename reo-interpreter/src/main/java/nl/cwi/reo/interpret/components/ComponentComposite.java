package nl.cwi.reo.interpret.components;

import java.util.Map;

import nl.cwi.reo.interpret.blocks.Program;
import nl.cwi.reo.interpret.blocks.Statement;
import nl.cwi.reo.interpret.ranges.Expression;
import nl.cwi.reo.interpret.ranges.ExpressionList;
import nl.cwi.reo.interpret.semantics.Definitions;
import nl.cwi.reo.interpret.semantics.InstanceList;
import nl.cwi.reo.interpret.signatures.SignatureConcrete;
import nl.cwi.reo.interpret.signatures.SignatureExpression;
import nl.cwi.reo.interpret.variables.VariableName;
import nl.cwi.reo.interpret.variables.VariableNameList;
import nl.cwi.reo.semantics.Semantics;

public class ComponentComposite<T extends Semantics<T>> implements ComponentExpression<T> {
	
	private SignatureExpression sign;
	
	private Statement<T> body;

	public ComponentComposite(SignatureExpression sign, Statement<T> body) {
		if (sign == null || body == null)
			throw new NullPointerException();
		this.sign = sign;
		this.body = body;
	}
	
	public ComponentExpression<T> evaluate(Map<VariableName, Expression> params) throws Exception {
		Statement<T> prog = body.evaluate(params);
		if (prog instanceof Program)
			return new ComponentValue<T>(sign, (Program<T>)prog);
		return new ComponentComposite<T>(sign, prog);
	}

	@Override
	public Statement<T> instantiate(ExpressionList values, VariableNameList iface) throws Exception {
		SignatureConcrete v = sign.evaluate(values, iface);
		Statement<T> _body = body.evaluate(v.getDefinitions());
		if (_body instanceof Program) {
			Definitions _definitions = new Definitions(((Program<T>)_body).getUnifications());
			InstanceList<T> _instances = new InstanceList<T>(((Program<T>)_body).getInstances());
			return new Program<T>(_definitions, _instances);
		}
		return _body;
	}
	
	@Override
	public String toString() {
		return "" + sign + body;
	}
}
