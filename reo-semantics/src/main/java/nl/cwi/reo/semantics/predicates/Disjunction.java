package nl.cwi.reo.semantics.predicates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.stringtemplate.v4.ST;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.typetags.TypeTag;
import nl.cwi.reo.util.Monitor;

/**
 * A disjunction of formulas.
 */
public final class Disjunction implements Formula {

	/**
	 * Flag for string template.
	 */
	public static final boolean disjunction = true;

	/**
	 * List of formulas in this disjunction.
	 */
	private final List<Formula> clauses;
	
	/**
	 * Free variables of this term.
	 */
	private final Set<Variable> vars;

	/**
	 * Constructs the disjunction of a list of formulas.
	 * 
	 * @param clauses
	 *            list of formulas
	 */
	public Disjunction(List<Formula> clauses) {
		this.clauses = Collections.unmodifiableList(new ArrayList<>(clauses));
		Set<Variable> vars = new HashSet<Variable>();
		for (Formula f : clauses)
			vars.addAll(f.getFreeVariables());
		this.vars = Collections.unmodifiableSet(vars);
	}

	/**
	 * Returns the list of clauses in this conjunction.
	 * 
	 * @return list of clauses of this conjunction.
	 */
	public List<Formula> getClauses() {
		return clauses;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Formula rename(Map<Port, Port> links) {
		List<Formula> h = new ArrayList<Formula>();
		for (Formula f : clauses)
			h.add(f.rename(links));
		return new Disjunction(h);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Port> getPorts() {
		Set<Port> P = new HashSet<Port>();
		for (Formula f : clauses)
			if (f instanceof Disjunction || f instanceof Conjunction || f instanceof Existential)
				P.addAll(f.getPorts());
		return P;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isQuantifierFree() {
		for (Formula f : clauses)
			if (!f.isQuantifierFree())
				return false;
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public @Nullable Formula evaluate(Scope s, Monitor m) {
		List<Formula> _clauses = new ArrayList<>();
		for (Formula f : clauses) {
			Formula g = f.evaluate(s, m);
			if (g == null)
				return null;
			_clauses.add(g);
		}
		return new Conjunction(_clauses);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Disjunction DNF() {
		List<Formula> list = new ArrayList<Formula>();
		for (Formula f : clauses)
			list.add(f.DNF());
		return new Disjunction(list);
	}
	
	@Override
	public Set<Set<Term>> inferTermType(Set<Set<Term>> termTypeSet) {
		for(Formula f : clauses)
			termTypeSet=f.inferTermType(termTypeSet);
		return termTypeSet;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Formula NNF() {
		List<Formula> list = new ArrayList<Formula>();

		if (clauses.size() == 1)
			return clauses.get(0).NNF();

		for (Formula f : clauses)
			list.add(f.NNF());
		return new Disjunction(list);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Formula substitute(Term t, Variable x) {
		if (!vars.contains(x))
			return this;
		List<Formula> _clauses = new ArrayList<>();
		for (Formula f : clauses)
			_clauses.add(f.substitute(t, x));
		return new Disjunction(_clauses);
	}
	

	@Override
	public Formula getTypedFormula(Map<Term, TypeTag> typeMap) {
		List<Formula> _clauses = new ArrayList<>();
		for (Formula f : clauses)
			_clauses.add(f.getTypedFormula(typeMap));
		return new Disjunction(_clauses);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Variable> getFreeVariables() {
		return vars;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<Variable, Integer> getEvaluation() {
		return new HashMap<Variable, Integer>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		ST st = new ST("(<clauses;separator=\" \u2228 \">)");
		st.add("clauses", clauses);
		return st.render();
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
		if (!(other instanceof Disjunction))
			return false;
		Set<Formula> s = new HashSet<>(this.getClauses());
		Set<Formula> s2 = new HashSet<>(((Disjunction) other).getClauses());

		return Objects.equals(s, s2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		Set<Formula> s = new HashSet<>(clauses);
		return Objects.hash(s);
	}

	
	@Override
	public Set<Set<Term>> getSynchronousSet() {
		Set<Set<Term>> s = new HashSet<>();
		for(Formula g : this.getClauses()){
			s.addAll(g.getSynchronousSet());
		}
		return s;
	}

}
