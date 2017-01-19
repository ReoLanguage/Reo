package nl.cwi.reo.interpret.variables;

import org.antlr.v4.runtime.Token;

import nl.cwi.reo.interpret.ranges.Range;

/**
 * A variable or variable range. All implementations of this interface are immutable. 
 */
public interface Variable extends Range {
	
	public Token getToken();
	
}
