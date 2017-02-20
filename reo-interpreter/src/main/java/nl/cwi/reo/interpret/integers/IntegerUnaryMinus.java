package nl.cwi.reo.interpret.integers;

import java.util.Map;

import nl.cwi.reo.errors.CompilationException;
import nl.cwi.reo.interpret.oldstuff.Expression;

public final class IntegerUnaryMinus implements IntegerExpression {

	private final IntegerExpression e;
	
	public IntegerUnaryMinus(IntegerExpression e) {
		if (e == null)
			throw new NullPointerException();
		this.e = e;
	}

	@Override
	public IntegerExpression evaluate(Map<String, Expression> params) throws CompilationException {
		IntegerExpression x = e.evaluate(params);
		if (x instanceof IntegerValue)
			return IntegerValue.unarymin((IntegerValue)x);
		return new IntegerUnaryMinus(x);
	}
	
	@Override
	public String toString() {
		return "-(" + e + ")";
	}
}
