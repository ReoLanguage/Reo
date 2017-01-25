package nl.cwi.reo.semantics.api;

public enum PortType {
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
