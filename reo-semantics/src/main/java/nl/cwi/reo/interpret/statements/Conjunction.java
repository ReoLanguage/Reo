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

		
		List<Scope> scopes =Arrays.asList(s); 
		Queue<PredicateExpression> stack = new LinkedList<PredicateExpression>(predicates);
		PredicateExpression P = null;
		List<Scope> extension = new ArrayList<Scope>();
		List<Scope> tmpList = new ArrayList<Scope>();
		int counter=0;
		
		while(!stack.isEmpty() && counter<=stack.size()){
			
			P = stack.poll();
			for(Scope si : scopes){
				List<Scope> list = P.evaluate(si, m); 
				if(list == null){
					counter++;
					stack.add(P);
					continue;
				}
				else if (list.equals(extension)) continue;
				else{
					counter=0;
					tmpList.addAll(list);
				}
				
			}
			extension=tmpList;
			tmpList = new ArrayList<Scope>();
			
			if (!extension.isEmpty()) {
				scopes = new ArrayList<Scope>();
				scopes.addAll(extension);
			}
		}
		
		return scopes;
	}

}
