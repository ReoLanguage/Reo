package nl.cwi.reo.semantics.symbolicautomata;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.util.Monitor;

public class Synchron implements Formula {
	
	private final Port p;
	
	public Synchron(Port p) {
		this.p = p;
	}

	public Port getPort() {
		return p;
	}

	@Override
	public Formula getGuard() {
		return this;
	}

	@Override
	public Map<Port, Term> getAssignment() {
		return new HashMap<Port, Term>();
	}

	@Override
	public Formula rename(Map<Port, Port> links) {
		Port b = links.get(p);
		if (b != null)
			return new Synchron(b);
		return this;
	}

	@Override
	public Set<Port> getInterface() {
		return new HashSet<Port>(Arrays.asList(p));
	}

	@Override
	public @Nullable Formula evaluate(Scope s, Monitor m) {
		return this;
	}

	@Override
	public Formula DNF() {
		return this;
	}
	
}
