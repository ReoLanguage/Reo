package nl.cwi.reo.semantics.predicates;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.util.Monitor;

public class Universal implements Formula {

	private final Variable x;
	
	private final Formula f;

	public Universal(Variable x, Formula f) {
		this.x = x;
		this.f = f;
	}
	
	public Variable getVariable() {
		return x;
	}

	@Override
	public Formula getGuard() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Map<Variable, Term> getAssignment() {
		return f.getAssignment();
	}

	@Override
	public Formula rename(Map<Port, Port> links) {
		Map<Port, Port> newlinks = new HashMap<Port, Port>(links);
		newlinks.remove(x); // this is pseudo code
		return new Universal(x, f.rename(newlinks));
	}

	@Override
	public Set<Port> getInterface() {
		Set<Port> P = f.getInterface();
		if (x instanceof Node)
			P.remove(((Node) x).getPort());
		return P;
	}

	public String toString() {
		return  "\u2200" + x + "(" + f + ")";  
	}
	
	@Override
	public @Nullable Formula evaluate(Scope s, Monitor m) {
		return null;
	}

	@Override
	public Disjunction DNF() {
		throw new UnsupportedOperationException("DNF assumes a quantifier free formula.");
	}

	@Override
	public Formula NNF() {
		return new Universal(x, f.NNF());
	}

	@Override
	public Formula QE() {
		return this;
	}

	@Override
	public Formula Substitute(Term t, Variable x) {
		if (this.x.equals(x))
			return this;
		return new Universal(x, f.Substitute(t, x));
	}

	@Override
	public Set<Variable> getFreeVariables() {
		Set<Variable> vars = f.getFreeVariables();
		vars.remove(x);
		return vars;
	}

	@Override
	public Map<Variable, Integer> getEvaluation() {
		Map<Variable, Integer> map = f.getEvaluation();
		map.remove(x);
		return map;
	}

}
