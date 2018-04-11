package nl.cwi.reo.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;

import nl.cwi.reo.semantics.predicates.Conjunction;
import nl.cwi.reo.semantics.predicates.Constant;
import nl.cwi.reo.semantics.predicates.Equality;
import nl.cwi.reo.semantics.predicates.Formula;
import nl.cwi.reo.semantics.predicates.Formulas;
import nl.cwi.reo.semantics.predicates.Function;
import nl.cwi.reo.semantics.predicates.MemoryVariable;
import nl.cwi.reo.semantics.predicates.Negation;
import nl.cwi.reo.semantics.predicates.NonNullValue;
import nl.cwi.reo.semantics.predicates.NullValue;
import nl.cwi.reo.semantics.predicates.PortVariable;
import nl.cwi.reo.semantics.predicates.Relation;
import nl.cwi.reo.semantics.predicates.Term;
import nl.cwi.reo.semantics.predicates.Terms;
import nl.cwi.reo.semantics.predicates.Variable;

/**
 * This class provides a static methods for commandification.
 */
public class Commands {

	public Set<Formula> constraints = new HashSet<>();
	public Set<Formula> guards = new HashSet<>();
	public Set<Set<Term>> updateSet = new HashSet<>();
	public List<Formula> others = new ArrayList<>();
	Map<Variable, Set<Term>> defs = new HashMap<>();
	
	public Commands(){
		
	}
	public Command getCommand(Formula f){
		recurseCommandify(f);
		// Express each variable in terms of input, current memory, functions, constants and null value
		Map<Variable, Term> update = new HashMap<>();
		for(Set<Term> set : updateSet){
			for(Term t : set){
				if((t instanceof MemoryVariable && !((MemoryVariable)t).hasPrime()
						|| t instanceof PortVariable && ((PortVariable)t).isInput()
						|| t instanceof Constant
						|| t instanceof Function
						|| t instanceof NullValue
						)&& !defs.containsKey(t)){
					for(Term term : set){
						if(term != t){
							for(Term outputOrMemory : defs.keySet()){
								if(outputOrMemory.equals(term))
									update.put((Variable)outputOrMemory, t);
								if(term instanceof PortVariable)
									guards.add(new Negation(new Equality(term, Terms.Null)));
							}
						}
					}
				}
			}
		}
		
		update = transitiveUpdate(update);
		
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
			if (isGuard)
				guards.add(_g);
			else
				constraints.add(_g);
		}
		Set<Variable> s = update.keySet();
		for(Variable v :s ){
			if(update.get(v) instanceof NonNullValue){
				update.remove(v);
			}
		}
		
		Formula guard = new Conjunction(guards);
		guard = composeCsemirings(guard);
		Formula constraint = new Conjunction(constraints);

		return new Command(guard, update, constraint);
	}
	
	public Map<Variable,Term> transitiveUpdate(Map<Variable,Term> update){
		
		Map<Variable,Term> _update = new HashMap<>();
		while (!_update.equals(update)){
			_update=new HashMap<>(update);
			for (Variable e : _update.keySet()) {
				for(Variable t : update.keySet()){
					if(update.get(t).getFreeVariables().contains(e)){
						update.put(t, update.get(t).substitute(_update.get(e),e));
						List<Formula> _others = new ArrayList<>();
						for(Formula formula : others){
							_others.add(formula.substitute(_update.get(e),e));
						}
						others=_others;
					}
					if(update.get(e).getFreeVariables().contains(t)){
						update.put(e, _update.get(e).substitute(_update.get(t), t));
						List<Formula> _others = new ArrayList<>();
						for(Formula formula : others){
							_others.add(formula.substitute(_update.get(t), t));
						}
						others=_others;
					}
				}
			}
		}
		
		return update;
	}
	
	
	public void recurseCommandify(Formula f){
		
		if(f instanceof Conjunction){
			for(Formula conjuncts : ((Conjunction) f).getClauses()){
				recurseCommandify(conjuncts);
			}
		}
		else if(f instanceof Equality){
			Equality e = (Equality) f;
			if(!(e.getRHS() instanceof Variable) && !(e.getLHS() instanceof Variable)){
				others.add(f);
				return;
			}
			if(e.getRHS() instanceof PortVariable && !(e.getLHS() instanceof PortVariable)){
				recurseCommandify(new Equality(e.getRHS(),e.getLHS()));
				return;
			}			
			if(e.getLHS() instanceof PortVariable) {
				PortVariable p = (PortVariable) e.getLHS();
				if(e.getRHS() instanceof NullValue)
					return;
				if(!p.isInput()){
					if(e.getRHS() instanceof MemoryVariable || e.getRHS() instanceof PortVariable){
						Variable v = (Variable) e.getRHS();
						if (v instanceof MemoryVariable && !((MemoryVariable)v).hasPrime()
								|| v instanceof PortVariable && ((PortVariable)v).isInput()) {
							update(p,v);
							return;
						} 
						else if(v instanceof MemoryVariable && ((MemoryVariable)v).hasPrime()
								|| v instanceof PortVariable && !((PortVariable)v).isInput()) {
							update(p,v);
							update(v,p);
							return;
						}
						else{
							others.add(f);
							return;
						}
					}
					else{
						update(p,e.getRHS());
						return;
					}
				}
				else{
					if(e.getRHS() instanceof MemoryVariable || e.getRHS() instanceof PortVariable){
						Variable v = (Variable) e.getRHS();
						if (v instanceof MemoryVariable && ((MemoryVariable)v).hasPrime()
								|| v instanceof PortVariable && !((PortVariable)v).isInput()) {
							update(v,p);
							return;
						} else{
							others.add(f);
							return;							
						}
					}
					else{
						others.add(f);
						return;
					}
				}
			}
			else if (e.getLHS() instanceof MemoryVariable) {
				MemoryVariable m = (MemoryVariable) e.getLHS();
				if(e.getRHS() instanceof MemoryVariable){
					MemoryVariable mRHS = (MemoryVariable) e.getRHS();
					if(!mRHS.hasPrime() && !m.hasPrime()){
						others.add(f);
						return;
					}
					if(mRHS.hasPrime()){
						update(mRHS,m);
					} 
					if(m.hasPrime()){
						update(m,mRHS);
					}
					return;
				}
				else if(m.hasPrime()){
					update(m,e.getRHS());
					return;	
				}
				else{
					others.add(f);
					return;
				}
			}
			return;
		} else if (f instanceof Negation && ((Negation) f).getFormula() instanceof Equality) {
			Equality e = (Equality) ((Negation) f).getFormula();
			if (e.getLHS() instanceof MemoryVariable && e.getRHS() instanceof NullValue) {
				if(!((MemoryVariable)e.getLHS()).hasPrime()){
					others.add(f);
				}return;
			} else if (e.getRHS() instanceof MemoryVariable && e.getLHS() instanceof NullValue) {
				if(!((MemoryVariable)e.getRHS()).hasPrime()){
					others.add(f);
				}return;
			}else if(e.getLHS() instanceof PortVariable && e.getRHS() instanceof NullValue){
				if(((PortVariable)e.getLHS()).isInput()){
					others.add(f);
				}
				return;
			}else if(e.getRHS() instanceof PortVariable && e.getLHS() instanceof NullValue){
				if(((PortVariable)e.getRHS()).isInput()){
					others.add(f);
				}
				return;
			}else if(e.getRHS() instanceof NonNullValue && e.getLHS() instanceof NullValue){
				return;
			}else if(e.getRHS() instanceof NullValue && e.getLHS() instanceof NonNullValue){
				return;
			}else {
				others.add(f);
				return;
			}
		}
		return;
	}

	public void update(Variable v, Term t){
		if (defs.get(v) == null) {
			Set<Term> set = new HashSet<>();
			set.add(t);
			defs.put(v, set);
		}else
			defs.get(v).add(t);
		if(t instanceof Function){
			others.add(new Negation(new Equality(t,Terms.Null)));
		}
		Set<Term> newSet = new HashSet<>();
		newSet.add(t);newSet.add(v);
		updateSet.add(newSet);
		Queue<Set<Term>> queue = new LinkedList<>(updateSet);
		while(!queue.isEmpty()){
			Set<Term> s = queue.poll();
			if((s.contains(v) || s.contains(t))){
				for(Set<Term> set : queue){
					if(set.contains(v) || set.contains(t)){
						updateSet.remove(s);
						updateSet.remove(set);
						s.addAll(set);
						updateSet.add(s);
					}
				}
			}
		}
	}
	
	
	
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
				if(!(e.getRHS() instanceof Variable) && !(e.getLHS() instanceof Variable)){
					others.add(L);
					continue;
				}
				
				if(e.getLHS() instanceof PortVariable) {
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
//						continue;
					}else if((e.getRHS() instanceof PortVariable) && ((PortVariable)e.getRHS()).isInput()){
						others.add(L);
						continue;
					}else if(e.getRHS() instanceof Constant){
						others.add(L);
						continue;
					}
				}
				if (e.getRHS() instanceof PortVariable) {
					PortVariable p = (PortVariable) e.getRHS();
					
					if(e.getLHS() instanceof NullValue)
						continue;
					
					if (!p.isInput()) {
						Set<Term> set = defs.get(p);
						if (set == null) {
							set = new HashSet<>();
							set.add(e.getLHS());
							defs.put(p, set);
						}
						set.add(e.getLHS());
//						continue;
					}else if((e.getLHS() instanceof PortVariable) && ((PortVariable)e.getLHS()).isInput()){
						others.add(L);
						continue;
					}else if(e.getLHS() instanceof Constant){
						others.add(L);
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
//						continue;
					} else
						others.add(L);
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
//						continue;
					}else
						others.add(L);
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
//						continue;
					}else{
						others.add(L);
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
//						continue;
					}else{
						others.add(L);
						continue;
					}
				}else if(e.getLHS() instanceof PortVariable && e.getRHS() instanceof NullValue){
					if(((PortVariable)e.getLHS()).isInput()){
						others.add(L);
					}
					continue;
				}else if(e.getRHS() instanceof PortVariable && e.getLHS() instanceof NullValue){
					if(((PortVariable)e.getRHS()).isInput()){
						others.add(L);
					}	
					continue;
				}else {
					others.add(L);
					continue;
				}
					
				
				
			} else
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
							if (!v.equals(u) && !(v instanceof NonNullValue))
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
		
		other :
		for (Formula g : others) {
			Formula _g = g;
			boolean isGuard = g.isQuantifierFree();				
			for (Variable v : g.getFreeVariables()) {
				if ((v instanceof PortVariable && !((PortVariable) v).isInput())
						|| (v instanceof MemoryVariable && ((MemoryVariable) v).hasPrime())) {
					Term t = update.get(v);
					if(t instanceof NonNullValue && g instanceof Equality){
						Equality e = (Equality) g;
						Term t1 = e.getLHS();
						Term t2 = e.getRHS();
						if(t1.equals(v))
							update.put(v, t2);
						if(t2.equals(v))
							update.put(v, t1);
						break other;
					}
					if (t != null)
						_g = g.substitute(t, v);
				}
			}
			if (isGuard)
				guards.add(_g);
			else
				constraints.add(_g);
		}
		Set<Variable> s = update.keySet();
		for(Variable v :s ){
			if(update.get(v) instanceof NonNullValue){
				update.remove(v);
			}
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
		guard = composeCsemirings(guard);
		Formula constraint = Formulas.conjunction(constraints);

		return new Command(guard, update, constraint);
	}
	
	public static Formula composeCsemirings(Formula f){
		//TODO: the guard is assumed to be a conjunction, extend to any formula
		//		the  
		Queue<Relation> relations = new LinkedList<>() ;
		List<Formula> newGuard = new ArrayList<>();
		if(f instanceof Conjunction){
			for(Formula l : ((Conjunction) f).getClauses()){
				if(l instanceof Relation){
					relations.add((Relation)l);
				}
				else
					newGuard.add(l);
			}
		}
		else 
			newGuard.add(f);
		Relation r1 = relations.poll();
		if(r1!=null){
			while(r1.getName().split("\\.").length>=2 && !r1.getName().split("\\.")[2].contains("1")){
				relations.add(r1);
				r1=relations.poll();
			}
			String name= r1.getName().substring(1, r1.getName().length()-1);		
			String n = name.split("\\.")[0];
			
			
			while(!relations.isEmpty()){
				Relation r2 = relations.poll();
				String nameComposition = "";
				if(r1.getName().split("\\.").length>=2)
					nameComposition = "*"+n+".lex.composition*";
				else
					nameComposition = "*"+n+".composition*";
				Function compositionValue = new Function(nameComposition,Arrays.asList(r1.getArgs().get(0),r2.getArgs().get(0)),false,r1.getArgs().get(0).getTypeTag());
				Function compositionThreshold = new Function(nameComposition,Arrays.asList(r1.getArgs().get(1),r2.getArgs().get(1)),false,r1.getArgs().get(1).getTypeTag());
				r1 = new Relation(r1.getName(),Arrays.asList(compositionValue,compositionThreshold),false);
			}
			newGuard.add(r1);
		}
		return new Conjunction(newGuard);
	}
	
}
