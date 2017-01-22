package nl.cwi.reo.interpret.signatures;

public enum PrioType {
	AMPERSANT, PLUS, NONE;
	
	@Override
	public String toString() {
		switch(this) {
		case AMPERSANT: return "&";
		case PLUS: return "+";
		case NONE: return "";
		default: return null;
		}
	}
}
