package nl.cwi.reo.interpret;

public final class TypeTag implements ParameterType {
	
	private final String tag;
	
	public TypeTag(String tag) {
		this.tag = tag;
	}

	public String name() {
		return tag;
	}
	
	@Override
	public boolean equalType(ParameterType t) {
	    if (!(t instanceof TypeTag)) return false;
	    TypeTag tag = (TypeTag)t;
	   	return this.tag.equals(tag.tag);
	}
	
}
