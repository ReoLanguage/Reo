package nl.cwi.reo.semantics.symbolicautomata;

import java.util.Map;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.util.Monitor;

public class Negation implements Formula {
	
	private final Formula f;

	public Negation(Formula f) {
		this.f = f;
	}

	@Override
	public Formula getGuard() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Map<Port, Term> getAssignment() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Formula rename(Map<Port, Port> links) {
		return new Negation(f.rename(links));
	}

	@Override
	public Set<Port> getInterface() {
		return f.getInterface();
	}

	@Override
	public @Nullable Formula evaluate(Scope s, Monitor m) {
		return null;
	}

	@Override
	public Formula DNF() {
		return null;
	}

}
