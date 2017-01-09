package nl.cwi.reo.semantics;

import java.util.Objects;

public final class Port {

	private final String name;

	private final PortType type;

	private final String tag;
	
	private final boolean hidden;

	public Port(String name) {
		if (name == null)
			throw new NullPointerException();
		this.name = name;
		this.type = PortType.UNKNOWN;
		this.tag = "";
		this.hidden = false;
	}
	
	public Port(String name, PortType type, String tag, boolean hidden) {
		if (name == null || type == null || tag == null)
			throw new NullPointerException();
		this.name = name;
		this.type = type;
		this.tag = tag;
		this.hidden = hidden;
	}

	public String getName() {
		return name;
	}
	
	public PortType getType() {
		return type;
	}
	
	public String getTypeTag() {
		return tag;
	}
	
	public boolean isHidden() {
		return hidden;
	}
	
	public Port hide() {
		return new Port(name, type, tag, true);
	}
	
	public Port rename(String name) {
		return new Port(name, type, tag, hidden);
	}
	
	public Port retype(PortType type) {
		return new Port(name, type, tag, hidden);
	}
	
	@Override
	public boolean equals(Object other) {
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof Port)) return false;
	    Port p = (Port)other;
	   	return Objects.equals(this.name, p.name);
	}
	
	@Override
	public int hashCode() {
	    return Objects.hash(this.name);
	}	
	
	@Override
	public String toString() {
		switch (type) {
		case IN: 
			return (hidden ? "*": "") + name + "?" + tag;
		case OUT: 
			return (hidden ? "*": "") + name + "!" + tag;
		default: 
			return (hidden ? "*": "") + name + ":" + tag;
		}
	}
	
}
