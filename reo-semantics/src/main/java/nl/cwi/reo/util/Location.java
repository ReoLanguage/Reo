package nl.cwi.reo.util;

import java.io.File;
import java.util.Objects;

import org.antlr.v4.runtime.Token;
import org.checkerframework.checker.nullness.qual.Nullable;

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
	public Location(Token token, String filename) {
		this.source = new File(filename).getName();
		this.line = token.getLine();
		this.column = token.getCharPositionInLine();		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(@Nullable Object other) {
		if (other == null)
			return false;
		if (other == this)
			return true;
		if (!(other instanceof Location))
			return false;
		Location p = (Location) other;
		return Objects.equals(this.source, p.source)&&Objects.equals(this.line, p.line)&&Objects.equals(this.column, p.column);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.source,this.line,this.source);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return source + ":" + line + "." + column; 
	}
}
