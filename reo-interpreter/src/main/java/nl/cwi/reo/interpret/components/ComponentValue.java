package nl.cwi.reo.interpret.components;

import java.util.Map;

import nl.cwi.reo.interpret.blocks.Program;
import nl.cwi.reo.interpret.ranges.Expression;
import nl.cwi.reo.interpret.ranges.ExpressionList;
import nl.cwi.reo.interpret.semantics.InstanceList;
import nl.cwi.reo.interpret.signatures.SignatureConcrete;
import nl.cwi.reo.interpret.signatures.SignatureExpression;
import nl.cwi.reo.interpret.variables.VariableName;
import nl.cwi.reo.interpret.variables.VariableNameList;
import nl.cwi.reo.semantics.Semantics;

public final class ComponentValue<T extends Semantics<T>> implements ComponentExpression<T> {
	
	/**
	 * Signature expression.
	 */
	private final SignatureExpression sign;
	
	/**
	 * Program.
	 */
	private final Program<T> prog;
	
	/**
	 * Constructs a new component value.
	 * @param sign
	 * @param prog
	 */
	public ComponentValue(SignatureExpression sign, Program<T> prog) {
		if (sign == null || prog == null)
			throw new NullPointerException();
		this.sign = sign;
		this.prog = prog;
	}
	
	public SignatureExpression getSignature() {
		return sign;
	}
	
	public InstanceList<T> getInstances() {
		return prog.getInstances();
	}

	@Override
	public ComponentValue<T> evaluate(Map<VariableName, Expression> params) throws Exception {
		return new ComponentValue<T>(sign, prog.evaluate(params));
	}

	@Override
	public Program<T> instantiate(ExpressionList values, VariableNameList iface) throws Exception {
		SignatureConcrete links = sign.evaluate(values, iface);
		Program<T> _prog = prog.instantiate(links);
		return _prog;
	}
	
	@Override
	public String toString() {
		return sign + "{" + prog + "}";
	}
}