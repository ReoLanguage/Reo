package nl.cwi.reo.interpret.predicates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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
	 * Return a new Predicate with conjunction of scope 
	 * {@inheritDoc}
	 */
	@Override
	public Predicate evaluate(Scope s, Monitor m) {
		
		List<Scope> scopes = Arrays.asList(s);
		
		if (predicates.isEmpty())
			return new Predicate(scopes);
		
		// Non-empty queue of unevaluated predicates.
		Queue<PredicateExpression> q = new LinkedList<PredicateExpression>(predicates);
		
		boolean go = true;
		while (go) {
			go = false;
			
			PredicateExpression P = q.poll();
			List<Scope> newscopes = extend(scopes, P, m);
			if (newscopes == null) {
				q.add(P);
			} else {
				if (newscopes.equals(scopes))
				scopes = newscopes;
			}
			
			// if scopes 
		}
		return new Predicate(scopes);
	}
	
	private List<Scope> extend(List<Scope> scopes, PredicateExpression P, Monitor m) {
		List<Scope> newscopes = new ArrayList<Scope>();
		for (Scope s : scopes) {
			Predicate list = P.evaluate(s, m);
			
//			s.extend();
		}
		return null;
	}

}
