package nl.cwi.reo.interpret.components;

import java.util.Map;

import nl.cwi.reo.interpret.programs.ProgramExpression;
import nl.cwi.reo.interpret.programs.ProgramValue;
import nl.cwi.reo.interpret.ranges.Expression;
import nl.cwi.reo.interpret.ranges.ExpressionList;
import nl.cwi.reo.interpret.semantics.InstanceList;
import nl.cwi.reo.interpret.signatures.Signature;
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
	private final ProgramValue<T> prog;
	
	/**
	 * Constructs a new component value.
	 * @param sign
	 * @param prog
	 */
	public ComponentValue(SignatureExpression sign, ProgramValue<T> prog) {
		if (sign == null || prog == null)
			throw new NullPointerException();
		this.sign = sign;
		this.prog = prog;
	}
	
	public InstanceList<T> getInstances() {
		return prog.getInstances();
	}

	@Override
	public ComponentValue<T> evaluate(Map<VariableName, Expression> params) throws Exception {
		return new ComponentValue<T>(sign, prog.evaluate(params));
	}

	@Override
	public ProgramExpression<T> instantiate(ExpressionList values, VariableNameList iface) throws Exception {
		Signature links = sign.evaluate(values, iface);
		ProgramValue<T> _prog = prog.instantiate(links);
		return _prog;
	}
	
	@Override
	public String toString() {
		return sign + "{" + prog + "}";
	}
}