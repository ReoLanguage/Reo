package nl.cwi.reo.interpret.integers;

import java.util.Map;

import org.antlr.v4.runtime.Token;

import nl.cwi.reo.errors.CompilationException;
import nl.cwi.reo.interpret.ranges.Expression;
import nl.cwi.reo.interpret.variables.VariableName;

public final class IntegerDivision implements IntegerExpression {

	private final IntegerExpression e1;
	
	private final IntegerExpression e2;
	
	private final Token operator;
	
	public IntegerDivision(IntegerExpression e1, IntegerExpression e2, Token operator) {
		if (e1 == null || e2 == null)
			throw new NullPointerException();
		this.e1 = e1;
		this.e2 = e2;
		this.operator = operator;
	}

	@Override
	public IntegerExpression evaluate(Map<VariableName, Expression> params) throws CompilationException {
		IntegerExpression x1 = e1.evaluate(params);
		IntegerExpression x2 = e2.evaluate(params);
		if (x1 instanceof IntegerValue && x2 instanceof IntegerValue)
			return IntegerValue.division((IntegerValue)x1, (IntegerValue)x2, operator);
		return new IntegerDivision(x1, x2, operator);
	}
	
	@Override
	public String toString() {
		return "(" + e1 + "/" + e2 + ")";
	}
}
