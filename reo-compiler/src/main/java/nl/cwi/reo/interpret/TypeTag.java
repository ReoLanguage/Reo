package nl.cwi.reo.interpret;

public class TypeTag implements ParameterType {
	
	private String tag;
	
	public TypeTag(String tag) {
		this.tag = tag;
	}

	public boolean equals(ParameterType t) {
		if (t instanceof TypeTag) {
			TypeTag x = (TypeTag)t;
			return x.tag.equals(tag);
		}
		return false;
	}

	public String name() {
		return tag;
	}
}
