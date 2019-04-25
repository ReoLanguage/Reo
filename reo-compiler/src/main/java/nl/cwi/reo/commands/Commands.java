package nl.cwi.reo.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

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
								//Cannot expect data from output ports.
								if(term instanceof PortVariable && ((PortVariable) term).isInput())
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
		Set<Formula> guards = new HashSet<>(this.guards);
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
		if(constraints.isEmpty()&& guards.isEmpty() && update.isEmpty())
			return null;
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
			//** "Normalization" of the formula
			if(e.getRHS() instanceof Variable && !(e.getLHS() instanceof Variable)){
				recurseCommandify(new Equality(e.getRHS(),e.getLHS()));
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
		}
		else if (f instanceof Relation) {
			guards.add(f);
		}		
		else if (f instanceof Negation && ((Negation) f).getFormula() instanceof Relation) {
			guards.add(f);
		}
		else if (f instanceof Negation && ((Negation) f).getFormula() instanceof Equality) {
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
	
	public static Formula composeCsemirings(Formula f){
		//TODO: the guard is assumed to be a conjunction, extend to any formula
		
		Queue<Relation> semiringRelations = new LinkedList<>() ;
		List<Formula> newGuard = new ArrayList<>();
		if(f instanceof Conjunction){
			for(Formula l : ((Conjunction) f).getClauses()){
				if(l instanceof Relation && ((Relation) l).getName().contains("Semiring")){
					semiringRelations.add((Relation)l);
				}
				else
					newGuard.add(l);
			}
		}
		else 
			newGuard.add(f);


			
/*		if(r1!=null){
			while(r1.getName().split("\\.").length>=2 && !r1.getName().split("\\.")[2].contains("1")){
				relations.add(r1);
				r1=relations.poll();
			}
			String name= r1.getName().substring(1, r1.getName().length()-1);		
			String n = name.split("\\.")[0];
*/		
		Relation composite = null;
		if(!semiringRelations.isEmpty())
			 composite = semiringRelations.poll();
				
		while(!semiringRelations.isEmpty()){
			Relation r2 = semiringRelations.poll();
			String nameComposition = composite.getName().concat(".join");
//			if(r1.getName().split("\\.").length>=2)
//				nameComposition = "*"+n+".lex.composition*";
//			else
//				nameComposition = "*"+n+".composition*";
			Function compositionValue = new Function(nameComposition,Arrays.asList(composite.getArgs().get(0),r2.getArgs().get(0)),false,composite.getArgs().get(0).getTypeTag());
			Function compositionThreshold = new Function(nameComposition,Arrays.asList(composite.getArgs().get(1),r2.getArgs().get(1)),false,composite.getArgs().get(1).getTypeTag());
			composite = new Relation(composite.getName(),Arrays.asList(compositionValue,compositionThreshold),false);
		}
		if(composite != null) {
			composite = new Relation(composite.getName()+".lowerEq",composite.getArgs(),false);
			newGuard.add(composite);
		}
		return new Conjunction(newGuard);
	}
	
}
