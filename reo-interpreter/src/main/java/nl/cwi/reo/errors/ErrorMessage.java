package nl.cwi.reo.errors;

public final class ErrorMessage {
	
	private final ErrorType type;
	
	private final String msg;
	
	private final int line;
	
	private final int column;
	
	public ErrorMessage(ErrorType type, String msg, int line, int column) {
		this.type = type;
		this.msg = msg;
		this.line = line;
		this.column = column;
	}

	public ErrorType getType() {
		return this.type;
	}

	public String getMessage() {
		return this.msg;
	}

	public int getLine() {
		return this.line;
	}

	public int getColumn() {
		return this.column;
	}
	
	@Override
	public String toString() {
		return type.name() + " at line " + line + ":" + column + ": " + msg; 
	}
}
