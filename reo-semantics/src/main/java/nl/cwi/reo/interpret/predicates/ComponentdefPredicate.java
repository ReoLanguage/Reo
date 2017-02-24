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
	
	/**
	 * The evaluation of a ComponentdefPredicate is a Predicate 
	 * where the instance of the component is added to the scope
	 * 
	 * @param var
	 * @param component
	 */
	@Override
	public Predicate evaluate(Scope s, Monitor m) {
//		Variable variable = var.evaluate(s, m);
//		ReoConnector<T> connector = component.evaluate(s, m);
//		
//		List<Scope> scopeList = new ArrayList<Scope>();
//		Scope scope = new Scope();
//		
//		Set<Set<Identifier>> set= new HashSet<Set<Identifier>>();
//		
//		set.add(connector.getIdentifiers());
//		
//		scope.put(new Identifier(variable.toString()), new Instances<T>(Arrays.asList(connector),set));
//		scopeList.add(scope);		
//		return new Predicate(scopeList);
		return null;
	}

}
