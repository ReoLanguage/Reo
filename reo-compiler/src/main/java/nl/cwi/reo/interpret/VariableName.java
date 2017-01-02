package nl.cwi.reo.interpret;

import java.util.Objects;

public final class VariableName implements Variable {

	private final String name;
	
	public VariableName() {
		this.name = "";
	}
	
	public VariableName(String name) {
		if (name == null)
			throw new IllegalArgumentException("Argument cannot be null.");
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public VariableName addPrefix(String prefix) {
		return new VariableName(prefix + name);
	}

	@Override
	public Variable evaluate(DefinitionList params) throws Exception {
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
}
