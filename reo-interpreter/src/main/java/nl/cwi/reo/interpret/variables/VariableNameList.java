package nl.cwi.reo.interpret.variables;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.Token;

import nl.cwi.reo.errors.CompilationException;
import nl.cwi.reo.interpret.ranges.Expression;
import nl.cwi.reo.interpret.ranges.Range;

/**
 * An immutable list of variable names.
 */
public final class VariableNameList implements Variable, Range {
	
	private final List<VariableName> list;
	
	private final Token token;
	
	public VariableNameList(List<VariableName> list, Token token) {
		if (list == null)
			throw new NullPointerException();
		this.list = Collections.unmodifiableList(list);
		this.token = token;
	}

	public List<VariableName> getList() {
		return list;
	}

	@Override
	public Token getToken() {
		return token;
	}

	@Override
	public VariableNameList evaluate(Map<String, Expression> params) throws CompilationException {
		return this;
	}
	
	@Override
	public String toString() {
		return "" + list;
	}
}
