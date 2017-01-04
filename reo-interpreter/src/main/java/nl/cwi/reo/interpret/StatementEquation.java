package nl.cwi.reo.interpret;

import java.util.Iterator;
import java.util.Map;

public final class StatementEquation implements Statement {

	private final Variable var;
	
	private final Value val;
	
	public StatementEquation(Variable var, Value val) {
		if (var == null || val == null)
			throw new IllegalArgumentException("Arguments cannot be null.");
		this.var = var;
		this.val = val;
	}

	@Override
	public ZDefinition evaluate(Map<VariableName, Expression> params) throws Exception {
		
		Variable var_p = var.evaluate(params);
		Value val_p = val.evaluate(params);
		
		ZDefinitionList definitions = new ZDefinitionList();
		
		if (var_p instanceof VariableName) {
			if (val_p instanceof Expression) {
				definitions.put((VariableName)var_p, (Expression)val_p);
				return definitions;
			} else if (val_p instanceof ExpressionList) {
				throw new Exception("Value " + val_p + " must be of type expression.");	
			} 
		} else if (var_p instanceof VariableNameList) {	
			if (val_p instanceof ExpressionList) {	
				Iterator<VariableName> var = ((VariableNameList) var_p).getList().iterator();
				Iterator<Expression> exp = ((ExpressionList)val_p).getList().iterator();				
				while (var.hasNext() && exp.hasNext()) definitions.put(var.next(), exp.next());
				return definitions;
				
			} else if (val_p instanceof Expression) {
				throw new Exception("Value " + val_p + " must be of type list.");				
			}
		} 
		
		return new StatementEquation(var_p, val_p);
	}	

	@Override
	public String toString() {
		return var + "=" + val;
	}
}
