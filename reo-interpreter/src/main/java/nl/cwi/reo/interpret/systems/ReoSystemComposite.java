package nl.cwi.reo.interpret.systems;

import java.util.Map;

import nl.cwi.reo.interpret.blocks.Assembly;
import nl.cwi.reo.interpret.blocks.ReoBlock;
import nl.cwi.reo.interpret.expressions.ValueList;
import nl.cwi.reo.interpret.signatures.SignatureConcrete;
import nl.cwi.reo.interpret.signatures.SignatureExpression;
import nl.cwi.reo.interpret.variables.VariableNameList;
import nl.cwi.reo.semantics.api.Expression;
import nl.cwi.reo.semantics.api.Semantics;

public class ReoSystemComposite<T extends Semantics<T>> implements ReoSystem<T> {
	
	private SignatureExpression sign;
	
	private ReoBlock<T> reoBlock;

	public ReoSystemComposite(SignatureExpression sign, ReoBlock<T> body) {
		if (sign == null || body == null)
			throw new NullPointerException();
		this.sign = sign;
		this.reoBlock = body;
	}
	
	public ReoSystem<T> evaluate(Map<String, Expression> params) {
		ReoBlock<T> prog = reoBlock.evaluate(params);
		if (prog instanceof Assembly)
			return new ReoSystemValue<T>(sign, (Assembly<T>)prog);
		return new ReoSystemComposite<T>(sign, prog);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReoBlock<T> instantiate(ValueList values, VariableNameList iface) {
		SignatureConcrete v = sign.evaluate(values, iface);
		ReoBlock<T> _body = reoBlock.evaluate(v.getDefinitions());
		if (_body instanceof Assembly)
			return ((Assembly<T>)_body).instantiate(v);
		return _body;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "" + sign + reoBlock;
	}
}
