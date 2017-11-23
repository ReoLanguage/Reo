package nl.cwi.reo.semantics.predicates;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class offers utility functions for formulas.
 */
public class Formulas {
	
	public final static Formula True = new TruthValue(true);

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

		if (Collections.disjoint(V, f.getFreeVariables()))
			return f;

		if (f instanceof Conjunction) {

			List<Formula> clauses = new ArrayList<>(((Conjunction) f).getClauses());
			
			// Determine in which clauses each variable occurs.
			Map<Variable, Set<Integer>> occur = new HashMap<>();
			for (int i = 0; i < clauses.size(); i++) {
				for (Variable v : clauses.get(i).getFreeVariables()) {
					Set<Integer> formulas = occur.get(v);
					if (formulas == null) {
						formulas = new HashSet<>();
						formulas.add(i);
						occur.put(v, formulas);
					} else
						formulas.add(i);
				}
			}

			// Eliminate all variables from V via substitution.
			for (Variable v : V) {

				Set<Integer> Cv = occur.get(v);

				// If v does not occur in the the formula, continue.
				if (Cv == null)
					continue;

				// Try to find a term t equal to v, with v not in t.
				// We prefer to find a term different from NonNullValue.
				Term t = null;
				Integer eq = null;
				for (Integer i : Cv) {
					Formula c = clauses.get(i);
					if (c instanceof Equality) {
						Equality e = (Equality) c;
						if (e.getLHS().equals(v) && !e.getRHS().getFreeVariables().contains(v)) {
							eq = i;
							t = e.getRHS();
							if (t instanceof NonNullValue)
								continue;
							break;
						}
						if (e.getRHS().equals(v) && !e.getLHS().getFreeVariables().contains(v)) {
							eq = i;
							t = e.getLHS();
							if (t instanceof NonNullValue)
								continue;
							break;
						}
					} else if (c instanceof Negation && ((Negation) c).getFormula() instanceof Equality) {
						Equality e = (Equality) ((Negation) c).getFormula();
						if (e.getLHS().equals(v) && e.getRHS() instanceof NullValue) {
							eq = i;
							t = Terms.NonNull;
							continue;
						}
						if (e.getRHS().equals(v) && !e.getLHS().getFreeVariables().contains(v)) {
							eq = i;
							t = Terms.NonNull;
							continue;
						}
					}
				}

				if (t == null) {
					Integer ex = clauses.size();
					List<Formula> list = new ArrayList<>();
					for (Integer i : Cv)
						list.add(clauses.get(i));
					clauses.add(new Existential(v, conjunction(list)));
					for (Integer i : Cv)
						clauses.set(i, True);
					Set<Variable> vars = new HashSet<>();
					for (Integer i : Cv)
						vars.addAll(clauses.get(i).getFreeVariables());
					for (Variable x : vars) {
						Set<Integer> formulas = occur.get(x);
						if (formulas != null) {
							formulas.removeAll(Cv);
							formulas.add(ex);
						}
					}
				} else {
					clauses.set(eq, True);
					Cv.remove(eq);
					occur.remove(v);
					for (Integer i : Cv) {
						clauses.set(i, clauses.get(i).substitute(t, v));
						for (Variable x : t.getFreeVariables()) {
							Set<Integer> formulas = occur.get(x);
							if (formulas != null)
								formulas.add(i);
						}
					}
				}
			}

			return conjunction(clauses);
		}

		if (f instanceof Equality)
			if (V.contains(((Equality) f).getLHS()) || V.contains(((Equality) f).getRHS()))
				return True;

		if (f instanceof Disjunction) {
			List<Formula> _clauses = new ArrayList<>();
			for (Formula c : ((Disjunction) f).getClauses())
				_clauses.add(eliminate(c, V));
			return new Disjunction(_clauses);
		}

		return f;
	}
	
	public static Formula order(Formula f){
		
		if(f instanceof Disjunction){
			
		}
		
		if(f instanceof Conjunction){
			
		}
		
		if(f instanceof Disjunction){
			
		}

		return f;
	}

	/**
	 * Constructs and simplifies the conjunction of a list of formulas. Because
	 * this method simplifies the conjunction, this method is preferred over the
	 * constructor method of a formula. The simplification 1) removes
	 * duplicates, 2) removes conjunctions with true, 3) shortcuts conjunctions
	 * with false, 4) unifies conjunctions of conjunctions, 5) avoids
	 * conjunctions with a single clause, and 6) eliminated trivial equalities.
	 * 
	 * @param clauses
	 *            list of formulas
	 * @return simplified conjunction of the list of formulas.
	 */
	public static Formula conjunction(Collection<Formula> clauses) {
		List<Formula> _clauses = new ArrayList<>();
		for (Formula f : clauses) {
			if (f instanceof TruthValue) {
				if (((TruthValue) f).getValue() == false)
					return new TruthValue(false);
			} else if (f instanceof Conjunction) {
				_clauses.addAll(((Conjunction) f).getClauses());
			} else if (!_clauses.contains(f)) {
				if (f instanceof Equality) {
					Equality E = (Equality) f;
					if(E.getLHS() instanceof NonNullValue){
						Formula _E = new Negation(new Equality(E.getRHS(),new NullValue()));
						if(!_clauses.contains(_E))
							_clauses.add(_E);
					}
					else if (E.getRHS() instanceof NonNullValue){
						Formula _E = new Negation(new Equality(E.getLHS(),new NullValue()));
						if(!_clauses.contains(_E))
							_clauses.add(_E);
					}
					else if (!E.getLHS().equals(E.getRHS()))
//					if (!E.getLHS().equals(E.getRHS()))
						_clauses.add(f);
				} else
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

	// public static Formula eliminateOld(Formula f, Collection<? extends
	// Variable> V) {
	//
	// if (V.isEmpty())
	// return f;
	//
	// if (f instanceof Conjunction) {
	//
	// SimpleConjunction sc = getEqualities(((Conjunction) f).getClauses());
	//
	// // Clauses of the final resulting formula.
	// List<Formula> clauses = sc.getRelations();
	// Set<Set<Term>> eqns_f = sc.getEqualities();
	//
	// // Partition eqns_f into parts that do not share variables.
	// Map<Set<Set<Term>>, Set<Variable>> partition = new HashMap<>();
	// for (Set<Term> E : eqns_f) {
	//
	// Set<Set<Term>> part = new HashSet<>();
	// part.add(E);
	//
	// Set<Variable> vars = new HashSet<>();
	// for (Term t : E)
	// vars.addAll(t.getFreeVariables());
	//
	// Iterator<Map.Entry<Set<Set<Term>>, Set<Variable>>> iter =
	// partition.entrySet().iterator();
	// while (iter.hasNext()) {
	// Map.Entry<Set<Set<Term>>, Set<Variable>> entry = iter.next();
	// if (!Collections.disjoint(entry.getValue(), vars)) {
	// part.addAll(entry.getKey());
	// vars.addAll(entry.getValue());
	// iter.remove();
	// }
	// }
	//
	// partition.put(part, vars);
	//
	// }
	//
	// for (Set<Set<Term>> part : partition.keySet()) {
	//
	// // Merge all intersecting sets of equalities.
	// Set<Set<Term>> eqns = new HashSet<>();
	// for (Set<Term> eq : part) {
	// Iterator<Set<Term>> iter = eqns.iterator();
	// while (iter.hasNext()) {
	// Set<Term> E = iter.next();
	// for (Term t : E) {
	// if (!t.getFreeVariables().isEmpty() && eq.contains(t)) {
	// eq.addAll(E);
	// iter.remove();
	// break;
	// }
	// }
	// }
	// eqns.add(eq);
	// }
	//
	// // Eliminate all variables from V via substitution.
	// for (Set<Term> E : part) {
	// Set<Variable> xs = new HashSet<>();
	// Term t = null;
	//
	// Iterator<Term> iter = E.iterator();
	// while (iter.hasNext()) {
	// Term u = iter.next();
	// if (u instanceof Variable && V.contains(u))
	// xs.add((Variable) u);
	// else
	// t = u;
	// }
	//
	// if (t != null && !xs.isEmpty()) {
	//
	// for (Variable v : xs)
	// if (!t.getFreeVariables().contains(v))
	// E.remove(v);
	//
	// if (E.size() <= 1)
	// E.clear();
	//
	// Map<Variable, Term> map = new HashMap<>();
	// for (Variable v : xs)
	// map.put(v, t);
	//
	// for (Set<Term> E1 : eqns)
	// if (E1 != E)
	// for (Term u : E1)
	// u.substitute(map);
	//
	// for (Formula c : clauses)
	// c.substitute(map);
	// }
	// }
	//
	// // Transform the equations back to a formula.
	// for (Set<Term> E : eqns)
	// if (E.size() >= 2)
	// clauses.add(new Relation("=", new ArrayList<>(E), true));
	// }
	//
	// return conjunction(clauses);
	// }
	//
	// if (f instanceof Equality)
	// if (V.contains(((Equality) f).getLHS()) || V.contains(((Equality)
	// f).getRHS()))
	// return new TruthValue(true);
	//
	// if (f instanceof Disjunction) {
	// List<Formula> list = new ArrayList<>();
	// for (Formula clause : ((Disjunction) f).getClauses()) {
	// Formula g = eliminate(clause, V);
	// if (g == null)
	// return null;
	// list.add(g);
	// }
	// return new Disjunction(list);
	// }
	//
	// return f;
	// }

}
