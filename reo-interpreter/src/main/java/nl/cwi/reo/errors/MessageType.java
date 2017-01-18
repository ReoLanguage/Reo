package nl.cwi.reo.errors;

public enum MessageType {
	INFO, WARNING, ERROR;

	@Override
	public String toString() {
		switch(this) {
		case INFO: return "[info]";
		case WARNING: return "[warning]";
		case ERROR: return "[error]";
		default: return null;
		}
	}
}
