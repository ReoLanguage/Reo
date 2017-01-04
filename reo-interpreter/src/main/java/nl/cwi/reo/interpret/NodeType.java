package nl.cwi.reo.interpret;

public enum NodeType {
	FREE, 
	SOURCE, 
	SINK, 
	MIXED;

	@Override
	public String toString() {
		switch(this) {
		case FREE: return "";
		case SOURCE: return "?";
		case SINK: return "!";
		case MIXED: return ":";
		default: throw new IllegalArgumentException();
		}
	}
}
