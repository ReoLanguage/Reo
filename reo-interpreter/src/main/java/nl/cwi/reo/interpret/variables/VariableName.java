package nl.cwi.reo.interpret.variables;

import java.util.Map;
import java.util.Objects;

import org.antlr.v4.runtime.Token;

import nl.cwi.reo.errors.CompilationException;
import nl.cwi.reo.interpret.ranges.Expression;

/**
 * An immutable implementation of a variable name.
 */
public final class VariableName implements Variable, Expression {

	private final String name;
	
	private final Token token;
	
	public VariableName(String name, Token token) {
		if (name == null)
			throw new NullPointerException();
		this.name = name;
		this.token = token;
	}
	
	public String getName() {
		return name;
	}

	@Override
	public Token getToken() {
		return token;
	}
	
	public VariableName addPrefix(String prefix) {
		return new VariableName(prefix + name, token);
	}

	@Override
	public Expression evaluate(Map<String, Expression> params) throws CompilationException {
		Expression e = params.get(this);
		if (e != null) return e;
		return this;
	}
	
	@Override
	public boolean equals(Object other) {
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof VariableName)) return false;
	    VariableName p = (VariableName)other;
	   	return Objects.equals(this.name, p.name);
	}
	
	@Override
	public int hashCode() {
	    return Objects.hash(this.name);
	}
	
	@Override
	public String toString() {
		return name;
	}
}
