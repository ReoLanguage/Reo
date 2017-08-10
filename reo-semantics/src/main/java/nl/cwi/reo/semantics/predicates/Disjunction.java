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

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.util.Monitor;

/**
 * Disjunction of a list of formulas.
 */
public class Disjunction implements Formula {

	/**
	 * Flag for string template.
	 */
	public static final boolean disjunction = true;

	/**
	 * List of formulas in this disjunction.
	 */
	private final List<Formula> clauses;
	
	/**
	 * Free variables in this formula.
	 */
	private final Set<Variable> freeVars;

	/**
	 * Constructs the disjunction of a list of formulas.
	 * 
	 * @param clauses
	 *            list of formulas
	 */
	public Disjunction(List<Formula> clauses) {
		this.clauses = clauses;
		Set<Variable> vars = new HashSet<Variable>();
		for (Formula c : clauses)
			vars.addAll(c.getFreeVariables());
		this.freeVars = Collections.unmodifiableSet(vars);
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
	public Set<Port> getInterface() {
		Set<Port> P = new HashSet<Port>();
		for (Formula f : clauses)
			if (f instanceof Disjunction || f instanceof Conjunction || f instanceof Existential)
				P.addAll(f.getInterface());
		return P;
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
	public Formula substitute(Map<Variable, Term> map) {
		if (Collections.disjoint(freeVars, map.keySet()))
			return this;
		List<Formula> list = new ArrayList<Formula>();
		if (clauses.size() == 1)
			return clauses.get(0).substitute(map);
		for (Formula f : clauses) {
			Formula formula = f.substitute(map);
			if (formula instanceof TruthValue && ((TruthValue) formula).getBool() == true)
				return formula;
			list.add(formula);
		}
		return new Disjunction(list);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Variable> getFreeVariables() {
		Set<Variable> vars = new HashSet<Variable>();
		for (Formula f : clauses)
			vars.addAll(f.getFreeVariables());
		return vars;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<Variable, Integer> getEvaluation() {
		// TODO Auto-generated method stub
		return new HashMap<Variable, Integer>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String s = "(" + clauses.get(0);
		for (int i = 1; i < clauses.size(); i++)
			s = s + " \u2228 " + clauses.get(i);
		return s + ")";
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

}
