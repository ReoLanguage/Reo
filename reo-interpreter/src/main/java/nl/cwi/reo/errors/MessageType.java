package nl.cwi.reo.errors;

public enum MessageType {
	INFO, WARNING, ERROR;

	@Override
	public String toString() {
		switch(this) {
		case INFO: return "[INFO]";
		case WARNING: return "[WARNING]";
		case ERROR: return "[ERROR]";
		default: return null;
		}
	}
}
