package nl.cwi.reo.semantics.symbolicautomata;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.util.Monitor;

public class BooleanValue implements Formula {
	
	private final boolean b;
	
	public BooleanValue(boolean b) {
		this.b = b;
	}
	
	public boolean getValue() {
		return b;
	}

	@Override
	public Formula getGuard() {
		return new BooleanValue(true);
	}

	@Override
	public Map<Variable, Term> getAssignment() {
		return new HashMap<Variable, Term>();
	}

	@Override
	public Formula rename(Map<Port, Port> links) {
		return this;
	}

	@Override
	public Set<Port> getInterface() {
		return new HashSet<Port>();
	}

	@Override
	public @Nullable Formula evaluate(Scope s, Monitor m) {
		return this;
	}

	@Override
	public Formula DNF() {
		return this;
	}

	@Override
	public Formula NNF(boolean isNegative) {
		
		return new BooleanValue(!isNegative);
	}

	@Override
	public Formula QE() {
		// TODO Auto-generated method stub
		return null;
	}

}
