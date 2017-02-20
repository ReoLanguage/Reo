package nl.cwi.reo.interpret.oldstuff;

public enum PortType {
	IN, OUT, NONE;
	
	@Override
	public String toString() {
		switch(this) {
		case IN: return "?";
		case OUT: return "!";
		case NONE: return "";
		default: return null;
		}
	}
}
