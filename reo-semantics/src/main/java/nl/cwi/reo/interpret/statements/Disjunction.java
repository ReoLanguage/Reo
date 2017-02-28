package nl.cwi.reo.interpret.statements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of a disjunction.
 */
public final class Disjunction implements PredicateExpression {

	/**
	 * List of disjuncts.
	 */
	private List<PredicateExpression> predicates;
	
	/**
	 * Constructs a new disjunction.
	 * @param predicates	list of disjuncts
	 */
	public Disjunction(List<PredicateExpression> predicates) {
		this.predicates = predicates;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Scope> evaluate(Scope s, Monitor m) {
		
//		List<Scope> scopes = Arrays.asList(s);
//		boolean updated = true;
//		boolean[] eval = new boolean[predicates.size()];
//		
//		while (updated) {
//			updated = false;
//			for (int i = 0; i < predicates.size(); i++) {
//				if (eval[i]) continue;
//				List<Scope> extension = new ArrayList<Scope>();
//				List<Scope> list = predicates.get(i).evaluate(s, m);
//				if (list.isEmpty()) {
//					extension = null;
//				} else {
//					for (Scope sl : list) {
//						if (extension.isEmpty()) {
//							extension.addAll(list);
//						} else {
//							if (!extension.get(0).keySet().equals(sl.keySet()))
//								return new ArrayList<Scope>();
//							if (!extension.contains(sl))
//								extension.add(sl);
//						}
//					}
//					eval[i] = true;
//				}				
//				if (extension != null) {
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
		List<Scope> scopes =Arrays.asList(s); 
		Queue<PredicateExpression> queue = new LinkedList<PredicateExpression>(predicates);
		PredicateExpression P = null;
		List<Scope> extension = new ArrayList<Scope>();
		
		while(!queue.isEmpty()){
			P = queue.poll();
			List<Scope> list = P.evaluate(s, m); 
			if(list == null){
				new Exception("Erreur in predicate"); 
			}
			else if (list.equals(s)) continue;
			else{
				extension.addAll(list);
			}
			if (!extension.isEmpty()) {
				scopes = extension;
			}
		}
		
		return scopes;
	}

}
