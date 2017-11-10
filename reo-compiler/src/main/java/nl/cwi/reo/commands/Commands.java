package nl.cwi.reo.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import nl.cwi.reo.semantics.predicates.Conjunction;
import nl.cwi.reo.semantics.predicates.Equality;
import nl.cwi.reo.semantics.predicates.Formula;
import nl.cwi.reo.semantics.predicates.Formulas;
import nl.cwi.reo.semantics.predicates.MemoryVariable;
import nl.cwi.reo.semantics.predicates.Negation;
import nl.cwi.reo.semantics.predicates.NonNullValue;
import nl.cwi.reo.semantics.predicates.NullValue;
import nl.cwi.reo.semantics.predicates.PortVariable;
import nl.cwi.reo.semantics.predicates.Term;
import nl.cwi.reo.semantics.predicates.Terms;
import nl.cwi.reo.semantics.predicates.Variable;

/**
 * This class provides a static methods for commandification.
 */
public class Commands {

	/**
	 * Transforms a given formula into a a command. The entries of the update
	 * map are ordered such that sequential evaluation is possible.
	 * 
	 * @param f
	 *            given formula
	 * @return command that corresponds to this formula.
	 */
	public static Command commandify(Formula f) {

		// Find the clauses of this formula.
		List<Formula> list = new ArrayList<>();
		if (f instanceof Conjunction)
			list.addAll(((Conjunction) f).getClauses());
		else
			list.add(f);

		// List of all formulas that do not define an output/memory variable.
		List<Formula> others = new ArrayList<>();

		// Find (possible implicit) definitions of outputs and memory value.
		Map<Variable, Set<Term>> defs = new HashMap<>();
		for (Formula L : list) {
			if (L instanceof Equality) {
				Equality e = (Equality) L;
				if (e.getLHS() instanceof PortVariable) {
					PortVariable p = (PortVariable) e.getLHS();
					// Remove p=* from commands 
					if(e.getRHS() instanceof NullValue)
						continue;
					
					if (!p.isInput()) {						
						Set<Term> set = defs.get(p);
						if (set == null) {
							set = new HashSet<>();
							set.add(e.getRHS());
							defs.put(p, set);
						}
						set.add(e.getRHS());
						continue;
					}
				}
				if (e.getLHS() instanceof MemoryVariable) {
					MemoryVariable m = (MemoryVariable) e.getLHS();
					if (m.hasPrime()) {
						Set<Term> set = defs.get(m);
						if (set == null) {
							set = new HashSet<>();
							set.add(e.getRHS());
							defs.put(m, set);
						}
						set.add(e.getRHS());
						continue;
					}
				}
				if (e.getRHS() instanceof PortVariable) {
					PortVariable p = (PortVariable) e.getRHS();
					if (!p.isInput()) {
						Set<Term> set = defs.get(p);
						if (set == null) {
							set = new HashSet<>();
							set.add(e.getLHS());
							defs.put(p, set);
						}
						set.add(e.getLHS());
						continue;
					}
				}
				if (e.getRHS() instanceof MemoryVariable) {
					MemoryVariable m = (MemoryVariable) e.getRHS();
					if (m.hasPrime()) {
						Set<Term> set = defs.get(m);
						if (set == null) {
							set = new HashSet<>();
							set.add(e.getLHS());
							defs.put(m, set);
						}
						set.add(e.getLHS());
						continue;
					}
				}
			} else if (L instanceof Negation && ((Negation) L).getFormula() instanceof Equality) {
				Equality e = (Equality) ((Negation) L).getFormula();
				if (e.getLHS() instanceof MemoryVariable && e.getRHS() instanceof NullValue) {
					MemoryVariable m = (MemoryVariable) e.getLHS();
					if (m.hasPrime()) {
						Set<Term> set = defs.get(m);
						if (set == null) {
							set = new HashSet<>();
							set.add(Terms.NonNull);
							defs.put(m, set);
						}
						set.add(Terms.NonNull);
						continue;
					}
				} else if (e.getRHS() instanceof MemoryVariable && e.getLHS() instanceof NullValue) {
					MemoryVariable m = (MemoryVariable) e.getRHS();
					if (m.hasPrime()) {
						Set<Term> set = defs.get(m);
						if (set == null) {
							set = new HashSet<>();
							set.add(Terms.NonNull);
							defs.put(m, set);
						}
						set.add(Terms.NonNull);
						continue;
					}
				}
			}

			others.add(L);
		}

		// Express each variable in terms of input and current memory
		Map<Variable, Term> update = new HashMap<>();
		boolean go;
		do {
			go = false;
			for (Map.Entry<Variable, Set<Term>> e : defs.entrySet()) {
				if (update.containsKey(e.getKey()))
					continue;
				for (Term u : e.getValue()) {
					// If there are alternatives, skip non-null value.
					if (u instanceof NonNullValue && e.getValue().size() > 1)
						continue;
					Term w = u;
					boolean foundNew = true;
					for (Variable v : u.getFreeVariables()) {
						if ((v instanceof PortVariable && !((PortVariable) v).isInput())
								|| (v instanceof MemoryVariable && ((MemoryVariable) v).hasPrime())) {
							Term t = update.get(v);
							if (t != null)
								w = u.substitute(t, v);
							else
								foundNew = false;
						}
					}
					if (foundNew) {
						go = true;
						update.put(e.getKey(), w);

						for (Term v : e.getValue())
							if (v != u && !(v instanceof NonNullValue))
								others.add(new Equality(w, v));
						
						// Atomic update with transitivity for functions : if 'a' is updated to 'b' and 'b' is updated to 'c', then 'a' is updated to 'c'
						for(Variable t : update.keySet()){
							if(update.get(t).getFreeVariables().contains(e.getKey())){
								update.put(t, update.get(t).substitute(w, e.getKey()));
								List<Formula> _others = new ArrayList<>();
								for(Formula formula : others){
									_others.add(formula.substitute(w, e.getKey()));
								}
								others=_others;
							}
							if(update.get(e.getKey()).getFreeVariables().contains(t)){
								update.put(e.getKey(), update.get(e.getKey()).substitute(update.get(t), t));
								List<Formula> _others = new ArrayList<>();
								for(Formula formula : others){
									_others.add(formula.substitute(update.get(t), t));
								}
								others=_others;
							}
							
						}

						break;
					}
				}
			}
		} while (go);

		// Add all failures to the list
		for (Map.Entry<Variable, Set<Term>> e : defs.entrySet())
			if (!update.containsKey(e.getKey()))
				for (Term t : e.getValue())
					others.add(new Equality(e.getKey(), t));

		// Construct the guard and constraint.
		Set<Formula> guards = new HashSet<>();
		Set<Formula> constraints = new HashSet<>();
		for (Formula g : others) {
			Formula _g = g;
			boolean isGuard = g.isQuantifierFree();
			for (Variable v : g.getFreeVariables()) {
				if ((v instanceof PortVariable && !((PortVariable) v).isInput())
						|| (v instanceof MemoryVariable && ((MemoryVariable) v).hasPrime())) {
					Term t = update.get(v);
					if (t != null)
						_g = g.substitute(t, v);
				}
			}
			if (isGuard)
				guards.add(_g);
			else
				constraints.add(_g);
		}
	
		// Put the memory cell and port check ahead of the conjunction.
//		LinkedList<Formula> gh = new LinkedList<>();
//		for(Formula g : guards){
//			for(Variable v : g.getFreeVariables()){
//				if(v instanceof Function){
//					gh.add(0,g);		
//				}
//			}
//		}
		
		Formula guard = Formulas.conjunction(guards);
		Formula constraint = Formulas.conjunction(constraints);

		return new Command(guard, update, constraint);
	}
}
