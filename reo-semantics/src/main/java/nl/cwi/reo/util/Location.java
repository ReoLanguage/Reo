package nl.cwi.reo.util;

import java.io.File;

import org.antlr.v4.runtime.Token;

/**
 * A location in a Reo source file.
 */
public final class Location {
	
	/**
	 * Name of Reo source file.
	 */
	private String source;
	
	/**
	 * Line number.
	 */
	private final int line;
	
	/**
	 * Column number (tabs are 1 column)
	 */
	private final int column;
	
	/**
	 * Constructs a new location.
	 * @param source	name of Reo source file
	 * @param line		line number
	 * @param column	column number
	 */
	public Location(String source, int line, int column) {
		this.source = source;
		this.line = line;
		this.column = column;
	}

	/**
	 * Constructs a new location from an ANTLR4 token.
	 * @param token		token
	 */
	public Location(Token token) {
		this.source = new File(token.getInputStream().getSourceName()).getName();
		this.line = token.getLine();
		this.column = token.getCharPositionInLine();		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return source + ":" + line + "." + column; 
	}
}
