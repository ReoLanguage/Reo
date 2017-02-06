package nl.cwi.reo.semantics.api;

import java.util.Objects;

public class Port implements Comparable<Port> {

	private final String name;

	private final PortType type;
	
	private final PrioType prio;

	private final String tag;
	
	private final boolean hidden;

	public Port(String name) {
		if (name == null)
			throw new NullPointerException();
		this.name = name;
		this.type = PortType.NONE;
		this.prio = PrioType.NONE;
		this.tag = "";
		this.hidden = false;
	}
	
	public Port(String name, PortType type, PrioType prio, String tag, boolean hidden) {
		if (name == null || type == null || prio == null || tag == null)
			throw new NullPointerException();
		this.name = name;
		this.type = type;
		this.prio = prio;
		this.tag = tag;
		this.hidden = hidden;
	}


	/**
	 * Joins this port to port origin by inheriting, if necessary, 
	 * the type, tag and visibility of port origin. This method is used 
	 * when ports in a block are instantiated via a signature. 
	 * @param origin		port
	 * @returns a copy of this port, with possibly its type, 
	 * tag and visibility inherited from port origin.
	 */
	public Port join(Port origin) {
		if (origin == null)
			throw new NullPointerException();
		PortType _type = origin.type == PortType.NONE ? type : origin.type;
		String _tag = origin.tag.equals("") ? tag : origin.tag;
		boolean _hidden = hidden || origin.hidden;
		return new Port(name, _type, PrioType.NONE, _tag, _hidden);
	}

	public String getName() {
		return name;
	}
	
	public PortType getType() {
		return type;
	}
	
	public PrioType getPrioType() {
		return prio;
	}
	
	public String getTypeTag() {
		return tag;
	}

	public boolean isHidden() {
		return hidden;
	}
	
	public Port rename(String name) {
		return new Port(name, type, prio, tag, hidden);		
	}
	
	public Port hide() {
		return new Port(name, type, prio, tag, true);
	}
	
	@Override
	public String toString() {
		return (hidden ? "*" : "") + prio + name + type + tag;
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
	public int compareTo(Port other) {
		return this.name.compareTo(other.name);
	}	
}
