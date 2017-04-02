package nl.cwi.reo.semantics.predicates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.util.Monitor;

public class Existential implements Formula {

	private final Variable x;
	
	private final Formula f;

	public Existential(Variable x, Formula f) {
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
	public Map<Port, Term> getAssignment() {
		return f.getAssignment();
	}

	@Override
	public Formula rename(Map<Port, Port> links) {
		Map<Port, Port> newlinks = new HashMap<Port, Port>(links);
		newlinks.remove(x); // this is pseudo code
		return new Existential(x, f.rename(newlinks));
	}

	@Override
	public Set<Port> getInterface() {
		Set<Port> P = f.getInterface();
		if (x instanceof Node)
			P.remove(((Node) x).getPort());
		return P;
	}

	public String toString(){
		return  "\u2203" + x + "(" + f + ")";  
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
		return new Existential(x, f.NNF());
	}

	@Override
	public Formula QE() {
		if (f instanceof Existential) {
			return new Existential(x, f.QE());
		} else if (f instanceof Disjunction) {
			List<Formula> list = new ArrayList<Formula>();
			for (Formula fi : ((Disjunction) f).getClauses())
				list.add(new Existential(x, fi));
			return new Disjunction(list).QE();
		} else if (f instanceof Conjunction) {
			
			// TODO This code incorrectly assumes that conjunctions are not nested.
			
			List<Formula> list = new ArrayList<Formula>();
			Term t = null;
			for (Formula fi : ((Conjunction) f).getClauses()) {
				if (fi instanceof Equality) {
					Equality e = (Equality) fi;
					if (e.getLHS().equals(x)) {
						t = e.getRHS();
					} else if (e.getRHS().equals(x)) {
						t =  e.getLHS();
					} else {
						list.add(fi);
					}
				}
			}
			if (t != null)
				return new Conjunction(list).Substitute(t, x);
		}
		return this;
	}

	@Override
	public Formula Substitute(Term t, Variable x) {
		if (this.x.equals(x))
			return this;
		return new Existential(x, f.Substitute(t, x));
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
