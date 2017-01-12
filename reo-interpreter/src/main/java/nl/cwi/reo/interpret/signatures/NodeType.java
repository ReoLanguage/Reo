package nl.cwi.reo.interpret.signatures;

public enum NodeType {
	SOURCE, 
	SINK, 
	MIXED;

	@Override
	public String toString() {
		switch(this) {
		case SOURCE: return "?";
		case SINK: return "!";
		case MIXED: return ":";
		default: throw new IllegalArgumentException();
		}
	}
}
