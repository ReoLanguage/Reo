package nl.cwi.reo.interpret.predicates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.values.BooleanValue;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.util.Location;
import nl.cwi.reo.util.Monitor;

public class BooleanPredicate implements PredicateExpression{

	private boolean bool;
	
	public BooleanPredicate(boolean bool){
		this.bool=bool;
	}
	
	@Override
	public Predicate evaluate(Scope s, Monitor m) {
		if(bool)
			return new Predicate(Arrays.asList(s));
		else{
			m.add(new Location("BooleanPredicate",25,51),"Fasle predicate");
			return new Predicate(null);
		}
		
	}

}
