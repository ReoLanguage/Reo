package nl.cwi.reo.interpret.predicates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of a conjunction.
 */
public final class Conjunction implements PredicateExpression {

	/**
	 * List of conjuncts.
	 */
	private List<PredicateExpression> predicates;
	
	/**
	 * Constructs a new conjunction.
	 * @param predicates 	list of conjuncts.
	 */
	public Conjunction(List<PredicateExpression> predicates) {
		this.predicates = predicates;
	}

	/**
	 * Return a new Predicate with conjunction of scope 
	 * {@inheritDoc}
	 */
	@Override
	public Predicate evaluate(Scope s, Monitor m) {
		
		Stack<Scope> scope = new Stack<Scope>();
		scope.push(s);
		Predicate p1;
		Predicate p2;
//		List<Scope> scopeListReference = Arrays.asList(s);
//		List<Scope> scopeList = new ArrayList<Scope>();
		
//		while(!scopeListReference.equals(scopeList) && ){
//			scopeList=scopeListReference;

		while(!scope.isEmpty()){
			Scope p = scope.pop();
			
			for(PredicateExpression predExpr : predicates){
				if((p1 = predExpr.evaluate(p, m))!=null){
					scopeList.addAll(p1.getScopes());						
				}
			}
		}
		
		
		return new Predicate(scopeList);
	}

}
