package nl.cwi.reo.semantics.predicates;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.util.Monitor;

// This class is be replaced by a nullary relation.
@Deprecated
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
	public Formula NNF() {
		return this;
	}

	@Override
	public Formula QE() {
		return this;
	}

	@Override
	public Formula Substitute(Term t, Variable x) {
		return this;
	}

	@Override
	public Set<Variable> getFreeVariables() {
		return new HashSet<Variable>();
	}

	@Override
	public Map<Variable, Integer> getEvaluation() {
		return new HashMap<Variable, Integer>();
	}

}
