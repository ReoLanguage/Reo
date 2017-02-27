package nl.cwi.reo.interpret.statements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

//		List<Scope> scopes = new ArrayList<Scope>();
//		scopes.add(s);
		List<Scope> scopes =Arrays.asList(s); 
		boolean updated = true;
		boolean[] eval = new boolean[predicates.size()];
		
		while (updated) {
			updated = false;
			for (int i = 0; i < predicates.size(); i++) {
				if (eval[i]) continue;
				List<Scope> extension = new ArrayList<Scope>();
				for (Scope si : scopes) {
					List<Scope> list = predicates.get(i).evaluate(si, m);
					if (list==null) {
						extension = null;
						break;
					} else {
						for (Scope sl : list) {
							if (extension.isEmpty()) {
								extension.addAll(list);
							} else {
								if (!extension.get(0).keySet().equals(sl.keySet()))
									return new ArrayList<Scope>();
								if (!extension.contains(sl))
									extension.add(sl);
							}
						}
						eval[i] = true;
					}
				}
				if (extension != null) {
//					for(Scope se : extension)
//						scopes.add(se);
					scopes = extension;
					updated = true;
				}
			}
		}

		for (int i = 0; i < predicates.size(); i++)
			if (eval[i] == false)
				return new ArrayList<Scope>();
		
		return scopes;
	}

}
