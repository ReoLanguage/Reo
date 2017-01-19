package nl.cwi.reo.interpret.booleans;

import java.util.Map;

import nl.cwi.reo.errors.CompilationException;
import nl.cwi.reo.interpret.ranges.Expression;
import nl.cwi.reo.interpret.variables.VariableName;

public final class BooleanValue implements BooleanExpression {
	
	private final boolean bool;
	
	public BooleanValue(boolean bool) {
		this.bool = bool;
	}
	
	public boolean toBoolean() {
		return this.bool;
	}

	@Override
	public BooleanExpression evaluate(Map<VariableName, Expression> params) throws CompilationException {
		return this;
	}
	
	public static BooleanValue conjunction(BooleanValue v1, BooleanValue v2) {
		return new BooleanValue(v1.bool && v2.bool);
	}
	
	public static BooleanValue disjunction(BooleanValue v1, BooleanValue v2) {
		return new BooleanValue(v1.bool || v2.bool);
	}

	public static BooleanValue negation(BooleanValue v) {
		return new BooleanValue(!v.bool);
	}
	
	@Override
	public String toString() {
		return "" + bool;
	}
}
