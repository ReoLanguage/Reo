package nl.cwi.reo.interpret.systems;

import java.util.Map;

import nl.cwi.reo.errors.CompilationException;
import nl.cwi.reo.interpret.blocks.Assembly;
import nl.cwi.reo.interpret.blocks.ReoBlock;
import nl.cwi.reo.interpret.ranges.Expression;
import nl.cwi.reo.interpret.ranges.ExpressionList;
import nl.cwi.reo.interpret.semantics.Definitions;
import nl.cwi.reo.interpret.semantics.ComponentList;
import nl.cwi.reo.interpret.signatures.SignatureConcrete;
import nl.cwi.reo.interpret.signatures.SignatureExpression;
import nl.cwi.reo.interpret.variables.VariableName;
import nl.cwi.reo.interpret.variables.VariableNameList;
import nl.cwi.reo.semantics.Semantics;

public class ReoSystemComposite<T extends Semantics<T>> implements ReoSystem<T> {
	
	private SignatureExpression sign;
	
	private ReoBlock<T> reoBlock;

	public ReoSystemComposite(SignatureExpression sign, ReoBlock<T> body) {
		if (sign == null || body == null)
			throw new NullPointerException();
		this.sign = sign;
		this.reoBlock = body;
	}
	
	public ReoSystem<T> evaluate(Map<VariableName, Expression> params) throws CompilationException {
		ReoBlock<T> prog = reoBlock.evaluate(params);
		if (prog instanceof Assembly)
			return new ReoSystemValue<T>(sign, (Assembly<T>)prog);
		return new ReoSystemComposite<T>(sign, prog);
	}

	@Override
	public ReoBlock<T> instantiate(ExpressionList values, VariableNameList iface) throws CompilationException {
		SignatureConcrete v = sign.evaluate(values, iface);
		ReoBlock<T> _body = reoBlock.evaluate(v.getDefinitions());
		if (_body instanceof Assembly) {
			Definitions _definitions = new Definitions(((Assembly<T>)_body).getUnifications());
			ComponentList<T> _instances = new ComponentList<T>(((Assembly<T>)_body).getInstances());
			return new Assembly<T>(_definitions, _instances);
		}
		return _body;
	}
	
	@Override
	public String toString() {
		return "" + sign + reoBlock;
	}
}
