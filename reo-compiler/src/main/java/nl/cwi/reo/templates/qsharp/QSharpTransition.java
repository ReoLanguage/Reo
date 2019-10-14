package nl.cwi.reo.templates.qsharp;

import nl.cwi.reo.semantics.predicates.*;
import nl.cwi.reo.templates.Transition;

import java.util.*;
import java.util.stream.Collectors;

// TODO: Auto-generated Javadoc

/**
 * The Class Transition.
 */
public class QSharpTransition extends Transition {
	/**
	 * Instantiates a new transition.
	 *
	 * @param guard
	 *            the guard
	 * @param update
	 *            the update
	 * @param constraint
	 *            the constraint
	 */
	public QSharpTransition(Formula guard, Map<Variable,Term> update, Formula constraint) {
		super(guard, update, constraint);
	}
	
	/**
	 * Gets the set of guards.
	 * 
	 * @return guard
	 */
	public Formula getGuard() {
		List<Formula> guards = new LinkedList<>();
		if(guard instanceof Conjunction){
			List<Formula> _guards = ((Conjunction) guard).getClauses().stream().filter(formula ->
				!(formula instanceof Negation && (((Equality) ((Negation) formula).getFormula()).getLHS() instanceof Function || ((Equality) ((Negation) formula).getFormula()).getLHS() instanceof PortVariable))
			).collect(Collectors.toList());
			for(Formula _f : _guards){
				if(_f instanceof Negation && ((Negation) _f).getFormula() instanceof Relation){
					guards.add(guards.size(),_f);
				}
				else if(_f instanceof Relation){
					guards.add(guards.size(),_f);
				}
				else if(_f instanceof Negation && ((Negation) _f).getFormula() instanceof Equality){
					Formula f = ((Negation)_f).getFormula();
					if((((Equality) f).getRHS() instanceof Function || ((Equality) f).getLHS() instanceof Function) 
						|| f instanceof Relation && !guards.contains(f)){
						guards.add(guards.size(),_f);
					}
					else if(!guards.contains(_f)){
						guards.add(0,_f);
					}
				}
				else
					guards.add(0,_f);
			}
		}
		return new Conjunction(guards);
	}
}
