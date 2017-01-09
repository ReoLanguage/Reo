package nl.cwi.reo.interpret;

import java.util.List;
import java.util.Map;

public final class ComponentValue implements ComponentExpression {
	
	/**
	 * Signature expression.
	 */
	private final Signature sign;
	
	/**
	 * Program.
	 */
	private final ProgramValue prog;
	
	/**
	 * Constructs a new component value.
	 * @param sign
	 * @param prog
	 */
	public ComponentValue(Signature sign, ProgramValue prog) {
		if (sign == null || prog == null)
			throw new NullPointerException();
		this.sign = sign;
		this.prog = prog;
	}
	
	public List<Instance> getInstances() {
		return prog.getInstances();
	}

	public ProgramValue instantiate(ExpressionList values, Interface iface) throws Exception {
		SignatureInstance v = sign.evaluate(values, iface);
		ProgramValue program = prog.join(v.getLinks());
		program = program.instantiate(v.getLinks());
//		System.out.println("Evaluate " + this + values + iface + " gives " + program);	
		return program;
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