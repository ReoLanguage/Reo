package nl.cwi.reo.interpret.predicates;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.components.ComponentExpression;
import nl.cwi.reo.interpret.connectors.Semantics;
import nl.cwi.reo.interpret.variables.VariableExpression;
import nl.cwi.reo.util.Monitor;

public class ComponentdefPredicate<T extends Semantics<T>> implements PredicateExpression {

	private VariableExpression var;
	
	private ComponentExpression<T> component;
	
	public ComponentdefPredicate(VariableExpression var, ComponentExpression<T> component){
		this.var=var;
		this.component=component;
	}

	@Override
	public Predicate evaluate(Scope s, Monitor m) {
		// TODO Auto-generated method stub
		return null;
	}

}
