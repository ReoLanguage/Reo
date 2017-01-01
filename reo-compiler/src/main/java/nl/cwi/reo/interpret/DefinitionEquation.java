package nl.cwi.reo.interpret;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class DefinitionEquation implements DefinitionExpression {

	/**
	 * Variable list.
	 */
	private final Variable var;

	/**
	 * Abstract value.
	 */
	private final Expression expr;

	/**
	 * Constructs an abstract definition
	 * @param var
	 * @param expr
	 */
	public DefinitionEquation(Variable var, Expression expr) {
		this.var = var;
		this.expr = expr;
	}
	
	/**
	 * Evaluates this definition into an assignment of values to variables.
	 * @param params 		parameter assignment
	 */
	public DefinitionExpression evaluate(DefinitionList params) throws Exception {		

		Variable var_p = var.evaluate(params);
		Expression exp_p = expr.evaluate(params);	
		DefinitionList definitions = new DefinitionList();
		
		if (var_p instanceof VariableName) {
			if (exp_p instanceof ExpressionList) {
				throw new Exception("Expression " + exp_p + " cannot be of type list.");	
			} else {
				definitions.put((VariableName)var_p, exp_p);
				
				return definitions;
			}
		} else if (var_p instanceof VariableList) {
			boolean var_pIsListOfVariableNames = true;
			VariableList lst = (VariableList)var_p;
			List<VariableName> vars = new ArrayList<VariableName>();
			for (Variable x : lst.getList()) {
				if (x instanceof VariableName) {
					vars.add((VariableName)x);
				} else {
					var_pIsListOfVariableNames = false;
				}
			}
			
			if (var_pIsListOfVariableNames) {
				if (exp_p instanceof ExpressionList) {	
					
					Iterator<VariableName> var = vars.iterator();
					Iterator<Expression> exp = ((ExpressionList)exp_p).getList().iterator();				
					
					while (var.hasNext() && exp.hasNext())
						definitions.put(var.next(), exp.next());
					
					if (var.hasNext() || exp.hasNext())
						throw new Exception("List sizes of " + var_p + " and " + exp_p + " do not match.");
					
					return definitions;
					
				} else {
					throw new Exception("Expression " + exp_p + " must be of type list.");				
				}
			}
		} 
		
		return new DefinitionEquation(var_p, exp_p);
	}
}
