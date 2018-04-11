package nl.cwi.reo.semantics.predicates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.typetags.TypeTag;
import nl.cwi.reo.util.Monitor;

/**
 * The Class Negation.
 */
public final class Negation implements Formula {

	/**
	 * Flag for string template.
	 */
	public static final boolean negation = true;

	/** 
	 * Original predicate. 
	 */
	private final Formula f;

	/**
	 * Constructs a new negation of an original predicate.
	 *
	 * @param f
	 *            original predicate
	 */
	public Negation(Formula f) {
		this.f = f;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Formula rename(Map<Port, Port> links) {
		return new Negation(f.rename(links));
	}

	/**
	 * Returns the predicate that is negated by this negation.
	 *
	 * @return the formula
	 */
	public Formula getFormula() {
		return f;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Port> getPorts() {
		return f.getPorts();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isQuantifierFree() {
		return f.isQuantifierFree();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public @Nullable Formula evaluate(Scope s, Monitor m) {
		Formula g = f.evaluate(s, m);
		if (g == null)
			return null;
		return new Negation(g);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Formula NNF() {
		if (f instanceof Negation) {
			return f.NNF();
		} else if (f instanceof TruthValue) {
			return new TruthValue(!((TruthValue) f).getValue());
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
		} else if (f instanceof Equality) {
			return this;
		}
		return f.NNF();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Formula DNF() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Formula substitute(Term t, Variable x) {
		return new Negation(f.substitute(t, x));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Variable> getFreeVariables() {
		return f.getFreeVariables();
	}

	/**
	 * {@inheritDoc}
	 */
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "\u00AC" + f;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(@Nullable Object other) {
		if (other == null)
			return false;
		if (other == this)
			return true;
		if (!(other instanceof Negation))
			return false;
		Negation n = (Negation) other;
		return Objects.equals(f, n.getFormula());
	}

	@Override
	public Set<Set<Term>> inferTermType(Set<Set<Term>> termTypeSet) {
		return f.inferTermType(termTypeSet);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(f);
	}

	@Override
	public Formula getTypedFormula(Map<Term, TypeTag> typeMap) {
		return new Negation(f.getTypedFormula(typeMap));
	}

	@Override
	public Map<Port, Boolean> getSynchronousMap() {
		Map<Port, Boolean> map = new HashMap<>();
		Map<Port, Boolean> _map = new HashMap<>();
		map = f.getSynchronousMap();
		for(Port p : map.keySet()){
			_map.put(p, !map.get(p));
		}
		return _map;
	}
}
