package nl.cwi.reo.semantics.constraintautomata;

import java.util.Collection;
import java.util.Map;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.util.Monitor;

public final class Conjunction implements Formula {

	private final Formula f1;
	
	private final Formula f2;
	
	public Conjunction(Formula f1, Formula f2) {
		this.f1 = f1;
		this.f2 = f2;
	}

	@Override
	public Formula evaluate(Scope s, Monitor m) {
		return new Conjunction(f1.evaluate(s, m), f2.evaluate(s, m));
	}

	@Override
	public String getInternalName(Collection<? extends Port> intface) {
		String x = f1.getInternalName(intface);
		if (x != null) return x;
		return f2.getInternalName(intface);
	}

	@Override
	public Term findAssignment(String variable) {
		Term t = f1.findAssignment(variable);
		if (t != null) return t;
		return f2.findAssignment(variable);
	}

	@Override
	public Formula substitute(String variable, Term expression) {
		return new Conjunction(f1.substitute(variable, expression), f2.substitute(variable, expression));
	}

	@Override
	public Formula rename(Map<Port, Port> links) {
		return new Conjunction(f1.rename(links), f2.rename(links));
	}
}
