package nl.cwi.reo.interpret;

import java.util.Map;
import java.util.Objects;

public final class VariableName implements Variable {

	private final String name;
	
	public VariableName() {
		this.name = "";
	}
	
	public VariableName(String name) {
		if (name == null)
			throw new NullPointerException();
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public VariableName addPrefix(String prefix) {
		return new VariableName(prefix + name);
	}

	@Override
	public Expression evaluate(Map<VariableName, Expression> params) throws Exception {
		Expression e = params.get(this);
		if (e instanceof Expression) 
			return (Expression)e;
		return this;
	}
	
	@Override
	public boolean equals(Object other) {
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof VariableName)) return false;
	    VariableName p = (VariableName)other;
	   	return Objects.equals(this.name, p.name);
	}
	
	@Override
	public int hashCode() {
	    return Objects.hash(this.name);
	}
	
	@Override
	public String toString() {
		return name;
	}
}
