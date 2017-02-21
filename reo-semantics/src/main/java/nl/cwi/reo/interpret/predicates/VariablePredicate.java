package nl.cwi.reo.interpret.predicates;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.variables.VariableExpression;
import nl.cwi.reo.util.Monitor;

public class VariablePredicate implements PredicateExpression {
	
	private VariableExpression var;
	
	public VariablePredicate(VariableExpression var){
		this.var=var;
	}
	
	@Override
	public Predicate evaluate(Scope s, Monitor m) {
		
		return null;
	}

}
