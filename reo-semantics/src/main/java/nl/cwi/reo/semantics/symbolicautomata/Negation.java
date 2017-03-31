package nl.cwi.reo.semantics.symbolicautomata;

import java.util.HashMap;
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
		return new Negation(f.getGuard());
	}

	@Override
	public Map<Variable, Term> getAssignment() {
		return new HashMap<Variable,Term>();
	}

	@Override
	public Formula rename(Map<Port, Port> links) {
		return new Negation(f.rename(links));
	}
	
	public Formula getFormula(){
		return f;
	}

	@Override
	public Set<Port> getInterface() {
		return null;
	}

	@Override
	public @Nullable Formula evaluate(Scope s, Monitor m) {
		return null;
	}
	
	public String toString(){
		return "!(" + f.toString() + ")";
	}

	@Override
	public Formula DNF() {
		return null;
	}

	@Override
	public Formula NNF(boolean isNegative) {
		return f.NNF(!isNegative);
	}

	@Override
	public Formula QE() {
		// TODO Auto-generated method stub
		return null;
	}

}
