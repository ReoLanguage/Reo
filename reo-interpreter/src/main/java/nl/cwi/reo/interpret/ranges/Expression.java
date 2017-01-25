package nl.cwi.reo.interpret.ranges;

import java.util.Map;

import nl.cwi.reo.errors.CompilationException;

public interface Expression extends Range {
	
	public Expression evaluate(Map<String, Expression> params) throws CompilationException;

}
