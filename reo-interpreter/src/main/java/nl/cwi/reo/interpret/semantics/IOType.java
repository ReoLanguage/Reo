package nl.cwi.reo.interpret.semantics;

public enum IOType {
	IN, OUT, UNKNOWN;

	@Override
	public String toString() {
		switch(this) {
		case IN: return "?";
		case OUT: return "!";
		case UNKNOWN: return "";
		default: return null;
		}
	}
}
