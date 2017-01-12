package nl.cwi.reo.semantics;

import java.util.Objects;

public class Port {

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
	
	/**
	 * Updates this port by inheriting, if necessary, the type, tag and visibility of port y.
	 * @param x		port
	 * @param y		port
	 * @returns a copy of port x, with possibly its type, tag and visibility inherited from port y.
	 */
	public Port update(Port y) {
		if (y == null)
			throw new NullPointerException();
		PortType _type = type == PortType.UNKNOWN ? y.type : type;
		String _tag = tag.equals("") ? y.tag : tag;
		boolean _hidden = hidden || y.hidden;
		return new Port(name, _type, _tag, _hidden);
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
	
	public Port rename(String name) {
		return new Port(name, type, tag, hidden);		
	}
	
	public Port hide() {
		return new Port(name, type, tag, true);
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
			return name + "?" + tag;
		case OUT: 
			return name + "!" + tag;
		default: 
			return name + ":" + tag;
		}
	}
	
}
