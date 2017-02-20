package nl.cwi.reo.interpret.booleans;

import java.util.Map;
import java.util.Objects;

import nl.cwi.reo.interpret.oldstuff.Expression;

public final class BooleanValue implements BooleanExpression {
	
	private final boolean bool;
	
	public BooleanValue(boolean bool) {
		this.bool = bool;
	}
	
	public boolean toBoolean() {
		return this.bool;
	}

	@Override
	public BooleanExpression evaluate(Map<String, Expression> params) {
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
	public boolean equals(Object other) {
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof BooleanValue)) return false;
	    BooleanValue p = (BooleanValue)other;
	   	return Objects.equals(this.bool, p.bool);
	}
	
	@Override
	public int hashCode() {
	    return Objects.hash(this.bool);
	}
	
	@Override
	public String toString() {
		return "" + bool;
	}
}
