package nl.cwi.reo.interpret.statements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import nl.cwi.reo.interpret.Scope;
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
	 * {@inheritDoc}
	 */
	@Override
	public List<Scope> evaluate(Scope s, Monitor m) {

//		List<Scope> scopes =Arrays.asList(s); 
//		boolean updated = true;
//		boolean[] eval = new boolean[predicates.size()];
//		
//		while (updated) {
//			updated = false;
//			for (int i = 0; i < predicates.size(); i++) {
//				if (eval[i]) continue;
//				List<Scope> extension = new ArrayList<Scope>();
//				for (Scope si : scopes) {
//					List<Scope> list = predicates.get(i).evaluate(si, m);
//					if (list==null || list.isEmpty()) {
//						extension = null;
//						break;
//					} else {
//						for (Scope sl : list) {
//							if (extension.isEmpty()) {
//								extension.addAll(list);
//							} else {
//								if (!extension.get(0).keySet().equals(sl.keySet()))
//									return new ArrayList<Scope>();
//								if (!extension.contains(sl))
//									extension.add(sl);
//							}
//						}
//						eval[i] = true;
//					}
//				}
//				if (extension != null) {
////					for(Scope se : extension)
////						scopes.add(se);
//					scopes = extension;
//					updated = true;
//				}
//			}
//		}
//
//		for (int i = 0; i < predicates.size(); i++)
//			if (eval[i] == false)
//				return new ArrayList<Scope>();
//	
//		
//		while(updated){
//			updated = false;
//			
//			
//		}
		
		List<Scope> scopes =Arrays.asList(s); 
		Stack<PredicateExpression> stack = new Stack<PredicateExpression>();
		for(PredicateExpression p : predicates)
			stack.push(p);
		PredicateExpression P = null;
		List<Scope> extension = new ArrayList<Scope>();
		
		while(!stack.isEmpty()){
			P = stack.pop();
			for(Scope si : scopes){
				List<Scope> list = P.evaluate(si, m); 
				if(list == null){
					stack.push(P);
					continue;
				}
				else if (list.isEmpty()) continue;
				else{
					extension=list;
				}
			}
			if (!extension.isEmpty()) {
				scopes = extension;
			}
		}
		
		return scopes;
	}

}
