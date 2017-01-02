package nl.cwi.reo.interpret;

import java.util.Objects;

public final class Port {

	private final String name;

	private final IOType type;

	private final TypeTag tag;
	
	private final boolean hidden;

	public Port(String name) {
		this.name = name;
		this.tag = new TypeTag("");
		this.type = IOType.FREE;
		this.hidden = true;
	}
	
	public Port(String name, IOType type, TypeTag tag, boolean hidden) {
		this.name = name;
		this.type = type;
		this.tag = tag;
		this.hidden = hidden;
	}
	
	public String getName() {
		return name;
	}
	
	public IOType getIOType() {
		return type;
	}
	
	public TypeTag getTypeTag() {
		return tag;
	}
	
	public boolean isHidden() {
		return hidden;
	}
	
	public Port hide() {
		return new Port(name, type, tag, true);
	}
	
	public Port addSuffix(String suffix) {
		return new Port(name + suffix, type, tag, hidden);
	}
	
	@Override
	public boolean equals(Object other) {
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof Port)) return false;
	    Port p = (Port)other;
	   	return Objects.equals(this.name, p.name) && 
	   			Objects.equals(this.hidden, p.hidden);
	}
	
	@Override
	public int hashCode() {
	    return Objects.hash(this.name, this.hidden);
	}	
	
}
