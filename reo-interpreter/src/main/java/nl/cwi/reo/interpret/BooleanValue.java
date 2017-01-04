package nl.cwi.reo.interpret;

import java.util.Map;


public final class BooleanValue implements BooleanExpression {
	
	private final boolean bool;
	
	public BooleanValue(boolean bool) {
		this.bool = bool;
	}
	
	public boolean toBoolean() {
		return this.bool;
	}

	@Override
	public BooleanExpression evaluate(Map<VariableName, Expression> params) throws Exception {
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
