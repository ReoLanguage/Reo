package nl.cwi.reo.interpret.variables;

import org.antlr.v4.runtime.Token;

import nl.cwi.reo.semantics.api.Expression;

/**
 * A variable or variable range. All implementations of this interface are immutable. 
 */
public interface Variable extends Expression {
	
	public Token getToken();
	
}
