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

public class Negation implements Formula {
	
	/**
	 * Flag for string template.
	 */
	public static final boolean negation = true;
	
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
	public Formula NNF() {
		if (f instanceof Negation) {
			return f.NNF();
		} else if (f instanceof BooleanValue) {
			return new BooleanValue(!((BooleanValue) f).getValue());
		} else if (f instanceof Conjunction) {
			List<Formula> list = new ArrayList<Formula>();
			for (Formula fi : ((Conjunction) f).getClauses())
				list.add(new Negation(fi));
			return new Disjunction(list).NNF();
		} else if (f instanceof Disjunction) {
			List<Formula> list = new ArrayList<Formula>();
			for (Formula fi : ((Disjunction) f).getClauses())
				list.add(new Negation(fi));
			return new Conjunction(list).NNF();
		} else if (f instanceof Existential) {
			Variable x = ((Existential) f).getVariable();
			return new Universal(x, f).NNF();
		} else if (f instanceof Universal) {
			Variable x = ((Universal) f).getVariable();
			return new Existential(x, f).NNF();
		}
		return f.NNF();
	}

	@Override
	public Formula DNF() {
		return this;
	}

	@Override
	public Formula QE() {
		return new Negation(f.QE());
	}

	@Override
	public Formula Substitute(Term t, Variable x) {
		return new Negation(f.Substitute(t, x));
	}

	@Override
	public Set<Variable> getFreeVariables() {
		return f.getFreeVariables();
	}

	@Override
	public Map<Variable, Integer> getEvaluation() {
		Map<Variable, Integer> map = new HashMap<Variable, Integer>();
		for (Map.Entry<Variable, Integer> entry : f.getEvaluation().entrySet()) {
			if (entry.getValue().intValue() == 0)
				map.put(entry.getKey(), 1);
			if (entry.getValue().intValue() == 1)
				map.put(entry.getKey(), 0);
		}
		return map;
	}
}
