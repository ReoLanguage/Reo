package nl.cwi.reo.interpret.typetags;

import nl.cwi.reo.interpret.variables.ParameterType;

public final class TypeTag implements ParameterType {
	
	private final String tag;
	
	public TypeTag() {
		this.tag = "";
	}
	
	public TypeTag(String tag) {
		if (tag == null)
			throw new NullPointerException();
		this.tag = tag;
	}

	public boolean isEmpty() {
		return tag.equals("");
	}
	
	@Override
	public boolean equalType(ParameterType t) {
	    if (!(t instanceof TypeTag)) return false;
	    TypeTag tag = (TypeTag)t;
	   	return this.tag.equals(tag.tag);
	}
	
	@Override
	public String toString() {
		return tag;
	}	
}
