package nl.cwi.reo.interpret.systems;

import java.util.Map;

import nl.cwi.reo.interpret.blocks.Assembly;
import nl.cwi.reo.interpret.expressions.ValueList;
import nl.cwi.reo.interpret.semantics.ComponentList;
import nl.cwi.reo.interpret.signatures.SignatureConcrete;
import nl.cwi.reo.interpret.signatures.SignatureExpression;
import nl.cwi.reo.interpret.variables.VariableNameList;
import nl.cwi.reo.semantics.api.Expression;
import nl.cwi.reo.semantics.api.Semantics;

public final class ReoSystemValue<T extends Semantics<T>> implements ReoSystem<T> {
	
	/**
	 * Signature expression.
	 */
	private final SignatureExpression sign;
	
	/**
	 * Program.
	 */
	private final Assembly<T> prog;
	
	/**
	 * Constructs a new component value.
	 * @param sign
	 * @param prog
	 */
	public ReoSystemValue(SignatureExpression sign, Assembly<T> prog) {
		if (sign == null || prog == null)
			throw new NullPointerException();
		this.sign = sign;
		this.prog = prog;
	}
	
	public SignatureExpression getSignature() {
		return sign;
	}
	
	public ComponentList<T> getInstances() {
		return prog.getInstances();
	}

	@Override
	public ReoSystemValue<T> evaluate(Map<String, Expression> params) {
		return new ReoSystemValue<T>(sign, prog.evaluate(params));
	}

	@Override
	public Assembly<T> instantiate(ValueList values, VariableNameList iface) {
		SignatureConcrete links = sign.evaluate(values, iface);
		Assembly<T> _prog = prog.instantiate(links);
		return _prog;
	}
	
	@Override
	public String toString() {
		return sign + "{" + prog + "}";
	}
}