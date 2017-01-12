package nl.cwi.reo.interpret.components;

import java.util.List;
import java.util.Map;

import nl.cwi.reo.interpret.arrays.Expression;
import nl.cwi.reo.interpret.arrays.ExpressionRange;
import nl.cwi.reo.interpret.programs.ProgramExpression;
import nl.cwi.reo.interpret.programs.ProgramValue;
import nl.cwi.reo.interpret.semantics.Instance;
import nl.cwi.reo.interpret.signatures.Interface;
import nl.cwi.reo.interpret.signatures.Signature;
import nl.cwi.reo.interpret.signatures.SignatureExpression;
import nl.cwi.reo.interpret.variables.VariableName;

public final class ComponentValue implements ComponentExpression {
	
	/**
	 * Signature expression.
	 */
	private final SignatureExpression sign;
	
	/**
	 * Program.
	 */
	private final ProgramValue prog;
	
	/**
	 * Constructs a new component value.
	 * @param sign
	 * @param prog
	 */
	public ComponentValue(SignatureExpression sign, ProgramValue prog) {
		if (sign == null || prog == null)
			throw new NullPointerException();
		this.sign = sign;
		this.prog = prog;
	}
	
	public List<Instance> getInstances() {
		return prog.getInstances();
	}

	@Override
	public ProgramExpression instantiate(ExpressionRange values, Interface iface) throws Exception {
		Signature links = sign.evaluate(values, iface);
		ProgramValue _prog = prog.instantiate(links);
		return _prog;
	}

	@Override
	public ComponentValue evaluate(Map<VariableName, Expression> params) throws Exception {
		return new ComponentValue(sign, prog.evaluate(params));
	}
	
	@Override
	public String toString() {
		return sign + "{" + prog + "}";
	}
}