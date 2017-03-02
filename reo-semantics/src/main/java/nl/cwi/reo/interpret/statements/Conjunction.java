package nl.cwi.reo.interpret.statements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.checkerframework.checker.nullness.qual.Nullable;

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
	@Nullable
	public List<Scope> evaluate(Scope s, Monitor m) {
	
		List<Scope> scopes = Arrays.asList(s); 
		Queue<PredicateExpression> stack = new LinkedList<PredicateExpression>(predicates);
		PredicateExpression P = null;
		List<Scope> extension = new ArrayList<Scope>();
		List<Scope> tmpList = new ArrayList<Scope>();
		int counter = 0;
		
		while (!stack.isEmpty() && counter<=stack.size()) {
			
			P = stack.poll();
			for (Scope si : scopes) {
				List<Scope> list = P.evaluate(si, m); 
				if (list == null) {
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
