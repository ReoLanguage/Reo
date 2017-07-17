package nl.cwi.reo.semantics.predicates;

import java.util.ArrayList;
import java.util.Arrays;
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

// TODO: Auto-generated Javadoc
/**
 * A conjunction of a list of formulas.
 */
public class Conjunction implements Formula {

	/**
	 * Flag for string template.
	 */
	public static final boolean conjunction = true;

	/**
	 * List of formulas in this conjunction.
	 */
	private final List<Formula> clauses;

	/**
	 * Constructs the conjunction of a list of formulas.
	 * 
	 * @param clauses
	 *            list of formulas
	 */
	public Conjunction(List<Formula> clauses) {
		this.clauses = clauses;
	}

	/**
	 * Constructs and simplifies the conjunction of a list of formulas. Because
	 * this method simplifies the conjunction, this method is preferred over the
	 * constructor method of a formula. The simplification 1) removes
	 * duplicates, 2) removes conjunctions with true, 3) shortcuts conjunctions
	 * with false, 4) unifies conjunctions of conjunctions, and 5) avoids
	 * conjunctions with a single clause.
	 * 
	 * @param clauses
	 *            list of formulas
	 * @return simplified conjunction of the list of formulas.
	 */
	public static Formula conjunction(List<Formula> clauses) {
		List<Formula> _clauses = new ArrayList<>();
		for (Formula f : clauses) {
			if (f instanceof TruthValue) {
				if (((TruthValue) f).getBool() == false)
					return new TruthValue(false);
			} else if (f instanceof Conjunction) {
				_clauses.addAll(((Conjunction) f).getClauses());
			} else if (!_clauses.contains(f)) {
				_clauses.add(f);
			}
		}
		switch (_clauses.size()) {
		case 0:
			return new TruthValue(true);
		case 1:
			return _clauses.get(0);
		default:
			return new Conjunction(_clauses);
		}
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
		return new Conjunction(h);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Port> getInterface() {
		Set<Port> P = new HashSet<Port>();
		for (Formula f : clauses)
			if (!(f instanceof Equality) && (f.getInterface() != null))
				P.addAll(f.getInterface());
		return P;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public @Nullable Formula evaluate(Scope s, Monitor m) {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Formula DNF() {
		List<List<Formula>> c = new ArrayList<List<Formula>>();
		for (Formula f : clauses) {
			Formula g = f.DNF();
			if (g instanceof Disjunction) {
				c.add(((Disjunction) g).getClauses());
			} else {
				c.add(Arrays.asList(g));
			}
		}
		ClausesIterator iter = new ClausesIterator(c);
		List<Formula> clauses = new ArrayList<Formula>();
		while (iter.hasNext()) {
			List<Formula> list = new ArrayList<Formula>();
			List<Formula> tuple = iter.next();
			for (Formula f : tuple) {
				if (f instanceof Conjunction) {
					list.addAll(((Conjunction) f).getClauses());
				} else {
					list.add(f);
				}
			}
			clauses.add(Conjunction.conjunction(list));
		}
		if (clauses.size() == 1)
			return clauses.get(0);
		if (clauses.isEmpty())
			return new TruthValue(true);
		return new Disjunction(clauses);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Formula NNF() {
		List<Formula> list = new ArrayList<Formula>();
		for (Formula f : clauses)
			if (f.NNF() != null)
				list.add(f.NNF());
		return new Conjunction(list);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Formula QE() {
		List<Formula> list = new ArrayList<Formula>();
		for (Formula f : clauses)
			list.add(f.QE());
		return new Conjunction(list);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Formula substitute(Term t, Variable x) {
		List<Formula> list = new ArrayList<Formula>();
		for (Formula f : clauses)
			list.add(f.substitute(t, x));
		return new Conjunction(list);
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
	public boolean equals(@Nullable Object other) {
		if (other == null)
			return false;
		if (other == this)
			return true;
		if (!(other instanceof Conjunction))
			return false;
		return Objects.equals(this.getClauses(), ((Conjunction) other).getClauses());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String s = "(" + clauses.get(0);
		for (int i = 1; i < clauses.size(); i++)
			s = s + "\u2227" + clauses.get(i);
		return s + ")";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(clauses);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<Variable, Integer> getEvaluation() {
		Map<Variable, Integer> map = new HashMap<Variable, Integer>();
		for (Formula f : clauses) {
			map.putAll(f.getEvaluation());
			// propagate the information. for example, (x=y).getEvaluation() is
			// empty and the value is determined by composition.
			// TODO this propagation is very restrictive, as is works only for
			// simple equations like x=y.
			if (f instanceof Equality) {
				Equality e = (Equality) f;
				if (e.getLHS() instanceof Variable && e.getRHS() instanceof Variable) {
					Integer p = map.get((Variable) e.getLHS());
					Integer q = map.get((Variable) e.getRHS());
					if (p != null && q == null) {
						map.put((Variable) e.getRHS(), p);
					} else if (q != null && p == null) {
						map.put((Variable) e.getLHS(), q);
					}
				}
			}
		}
		return map;
	}

}
