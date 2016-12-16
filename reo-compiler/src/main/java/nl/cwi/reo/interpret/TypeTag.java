package nl.cwi.reo.interpret;

import nl.cwi.reo.automata.State;

public class TypeTag implements ParameterType {
	
	private String tag;
	
	public TypeTag(String tag) {
		this.tag = tag;
	}

	public String name() {
		return tag;
	}
	
	@Override
	public boolean equals(Object other) {
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof TypeTag)) return false;
	    TypeTag t = (TypeTag)other;
	   	return this.tag.equals(t.tag);
	}
	
    @Override
    public int hashCode() {
        return this.tag.hashCode();
    }
	
}
