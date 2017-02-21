package nl.cwi.reo.interpret.predicates;

import java.util.List;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.parameters.ParameterExpression;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.util.Monitor;

public class StructdefnPredicate implements PredicateExpression {

	private Identifier identifier;
	
	private List<ParameterExpression> params;
	
	public StructdefnPredicate(Identifier identifier, List<ParameterExpression> params){
		this.identifier=identifier;
		this.params=params;
	}
	
	@Override
	public Predicate evaluate(Scope s, Monitor m) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
