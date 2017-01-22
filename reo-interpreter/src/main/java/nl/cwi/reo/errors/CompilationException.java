package nl.cwi.reo.errors;

import org.antlr.v4.runtime.Token;

public class CompilationException extends RuntimeException {
	
	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = -5533171308171586072L;
	
	private final Token token;
	
	private final String msg;
	
	public CompilationException(Token token, String msg) {
		this.token = token;
		this.msg = msg;
	}
	
	public Token getToken() {
		return token;
	}
	
	@Override
	public String getMessage() {
		return msg;
	}
}
