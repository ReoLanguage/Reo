package nl.cwi.reo.interpret.predicates;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.util.Monitor;

public class BooleanPredicate implements PredicateExpression{

	private boolean bool;
	
	public BooleanPredicate(boolean bool){
		this.bool=bool;
	}
	
	@Override
	public Predicate evaluate(Scope s, Monitor m) {
		
		return null;
	}

}
