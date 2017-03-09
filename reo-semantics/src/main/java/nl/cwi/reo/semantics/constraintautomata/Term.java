package nl.cwi.reo.semantics.constraintautomata;

import java.util.Map;

import nl.cwi.reo.interpret.Expression;
import nl.cwi.reo.interpret.ports.Port;

public interface Term extends Expression<Term> {
	
	public boolean contains(String variable);
	
	public Term substitute(String variable, Term expression);

	public Term rename(Map<Port, Port> links);
	
}
