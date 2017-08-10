package nl.cwi.reo.semantics.predicates;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Formulas {

	/**
	 * Eliminates, if possible, all occurrences of a given set of variables via
	 * existential quantification.
	 * 
	 * <p>
	 * This method assumes that the formula is quantifier free and in
	 * disjunctive normal form. This method operates on literals of the form
	 * <code>!(x = *)</code> and of the form <code>x = F(y1, ...,yn)</code>,
	 * where <code>x, y1, ..., yn</code> are some (not necessarily distinct)
	 * variables, and <code>f</code> is a function that maps only <code>*</code>
	 * to <code>*</code> (that is,
	 * <code>F(y1, ...,yn) = * implies y1 = * || ... || yn = *</code>). Such
	 * literals are used to express each variable in terms of variables outside
	 * of <code>V</code>.
	 * 
	 * <p>
	 * Eliminating a variable <code>x</code> from a formula that contains a
	 * conjunct of the form <code>!(x = *)</code>, replaces every occurrence of
	 * <code>x</code> by a term
	 * {@link nl.cwi.reo.semantics.predicates.NonNullValue} that represents an
	 * arbitrary non-null datum.
	 * 
	 * <p>
	 * Clearly, this procedure is incomplete in the sense that it fails to
	 * eliminate variables from particular formulas. For example, to eliminate
	 * any variable from <code>R(x,y)</code>, we need additional information
	 * about relation <code>R</code> and the domain of the variables.
	 * 
	 * <p>
	 * The complexity of this method is <code>O(L*F^2)</code>, where
	 * <code>L</code> is the total number of literals in <code>f</code> and
	 * <code>F</code> the number of free variables in <code>f</code>.
	 * 
	 * @param f
	 *            quantifier free formula
	 * @param V
	 *            set of variables to eliminate
	 * 
	 * @return Quantifier free formula without free variables in <code>V</code>,
	 *         or null, if not all variables could be eliminated.
	 */
	public static Formula eliminate(Formula f, Collection<? extends Variable> V) {

		if (f instanceof Conjunction) {

			// Clauses of the final resulting formula.
			List<Formula> clauses = new ArrayList<Formula>();

			// Partition the terms in equalities in f
			Set<Set<Term>> eqns_f = new HashSet<>();
			for (Formula L : ((Conjunction) f).getClauses()) {
				if (L instanceof Equality) {
					Equality e = (Equality) L;
					Set<Term> eq = new HashSet<>();
					eq.add(e.getLHS());
					eq.add(e.getRHS());
					eqns_f.add(eq);
					continue;
				} else if (L instanceof Negation) {
					Negation neg = (Negation) L;
					if (neg.getFormula() instanceof Equality) {
						Equality e = (Equality) neg.getFormula();
						if (e.getLHS() instanceof Variable && e.getRHS() instanceof NullValue) {
							Set<Term> eq = new HashSet<>();
							eq.add(e.getLHS());
							eq.add(new NonNullValue());
							eqns_f.add(eq);
							continue;
						} else if (e.getRHS() instanceof Variable && e.getLHS() instanceof NullValue) {
							Set<Term> eq = new HashSet<>();
							eq.add(e.getRHS());
							eq.add(new NonNullValue());
							eqns_f.add(eq);
							continue;
						}
					}
				}

				// If we get here, L is not recognized, and we skip it.
				clauses.add(L);
			}

			// Partition eqns_f into parts that do not share variables.
			Map<Set<Set<Term>>, Set<Variable>> partition = new HashMap<>();
			for (Set<Term> E : eqns_f) {

				Set<Set<Term>> part = new HashSet<>();
				part.add(E);

				Set<Variable> vars = new HashSet<>();
				for (Term t : E)
					vars.addAll(t.getFreeVariables());

				Iterator<Map.Entry<Set<Set<Term>>, Set<Variable>>> iter = partition.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry<Set<Set<Term>>, Set<Variable>> entry = iter.next();
					if (!Collections.disjoint(entry.getValue(), vars)) {
						part.addAll(entry.getKey());
						vars.addAll(entry.getValue());
						iter.remove();
					}
				}

				partition.put(part, vars);

			}

			for (Set<Set<Term>> part : partition.keySet()) {

				// Merge all intersecting sets of equalities.
				Set<Set<Term>> eqns = new HashSet<>();
				for (Set<Term> eq : part) {
					Iterator<Set<Term>> iter = eqns.iterator();
					while (iter.hasNext()) {
						Set<Term> E = iter.next();
						for (Term t : E) {
							if (!t.getFreeVariables().isEmpty() && eq.contains(t)) {
								eq.addAll(E);
								iter.remove();
								break;
							}
						}
					}
					eqns.add(eq);
				}

				// Eliminate all variables from V via substitution.
				Iterator<Set<Term>> iterE = part.iterator();
				while (iterE.hasNext()) {
					Set<Term> E = iterE.next();

					Set<Variable> xs = new HashSet<>();
					Term t = null;

					Iterator<Term> iter = E.iterator();
					while (iter.hasNext()) {
						Term u = iter.next();
						if (u instanceof Variable && V.contains(u)) {
							xs.add((Variable) u);
						} else {
							t = u;
						}
					}

					if (t != null && !xs.isEmpty()) {

						for (Variable v : xs)
							if (!t.getFreeVariables().contains(v))
								E.remove(v);

						if (E.size() <= 1)
							E.clear();

						Map<Variable, Term> map = new HashMap<>();
						for (Variable v : xs)
							map.put(v, t);

						for (Set<Term> E1 : eqns)
							if (E1 != E)
								for (Term u : E1)
									u.substitute(map);

						for (Formula c : clauses)
							c.substitute(map);
					}
				}

				// Transform the equations back to a formula.
				for (Set<Term> E : eqns)
					clauses.add(new Relation("=", new ArrayList<>(E)));
			}

			return conjunction(clauses);
		}

		if (f instanceof Equality)
			if (V.contains(((Equality) f).getLHS()) || V.contains(((Equality) f).getRHS()))
				return new TruthValue(true);

		if (f instanceof Disjunction) {
			List<Formula> list = new ArrayList<>();
			for (Formula clause : ((Disjunction) f).getClauses()) {
				Formula g = eliminate(clause, V);
				if (g == null)
					return null;
				list.add(g);
			}
			return new Disjunction(list);
		}

		return f;
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
}
