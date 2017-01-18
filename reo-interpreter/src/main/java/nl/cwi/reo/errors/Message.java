package nl.cwi.reo.errors;

import org.antlr.v4.runtime.Token;

public final class Message {
	
	private final MessageType type;
	
	private String source;
	
	private final int line;
	
	private final int column;
	
	private final String msg;
	
	public Message(MessageType type, String source, int line, int column, String msg) {
		this.type = type;
		this.source = source;
		this.line = line;
		this.column = column;
		this.msg = msg;
	}
	
	public Message(MessageType type, Token token,  String msg) {
		this.type = type;
		this.source = token.getInputStream().getSourceName();
		this.line = token.getLine();
		this.column = token.getCharPositionInLine();
		this.msg = msg;
	}
	
	@Override
	public String toString() {
		return type.name() + " in " + source + " at line " + line + ":" + column + ": " + msg; 
	}
}
