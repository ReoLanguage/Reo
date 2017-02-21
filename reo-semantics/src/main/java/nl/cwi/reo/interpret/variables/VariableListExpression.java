package nl.cwi.reo.interpret.variables;

import java.util.ArrayList;
import java.util.List;

import nl.cwi.reo.interpret.Expression;
import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of a list of variable expressions of generic type, 
 * such as lists of ports, lists of nodes, and lists of parameters.
 * @param <I> type of identifier
 */
public final class VariableListExpression implements Expression<List<Variable>> {

	/**
	 * List of variable expressions.
	 */
	private final List<VariableExpression> list;

	/**
	 * Constructs a new list of variable expressions.
	 * @param list	list of variable expressions
	 */
	public VariableListExpression(List<VariableExpression> list) {
		this.list = list;
	}

	@Override
	public List<Variable> evaluate(Scope s, Monitor m) {
		List<Variable> l = new ArrayList<Variable>();
		for(VariableExpression v : list){
			l.add(v.evaluate(s, m));
		}
		
		return l;
	}
	
	/**
	 * {@inheritDoc}
	 */
//	@Override
//	public IdentifierList<I> evaluate(Scope s, Monitor m) {
//		List<I> concatenation = new ArrayList<I>();
//		for (VariableExpression<I> ve : list)
//			concatenation.addAll(ve.evaluate(s, null).getIdentifiers());
//		return new IdentifierList<I>(concatenation);
//	}

}
