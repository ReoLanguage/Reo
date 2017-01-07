package nl.cwi.reo.interpret;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class ProgramEquation implements ProgramExpression {

	private final Variable var;
	
	private final Value val;
	
	public ProgramEquation(Variable var, Value val) {
		if (var == null || val == null)
			throw new IllegalArgumentException("Arguments cannot be null.");
		this.var = var;
		this.val = val;
	}

	@Override
	public ProgramExpression evaluate(Map<VariableName, Expression> params) throws Exception {
		
		Variable var_p = var.evaluate(params);
		Value val_p = val.evaluate(params);
		
		Map<VariableName, Expression> definitions = new HashMap<VariableName, Expression>();
		
		if (var_p instanceof VariableName) {
			if (val_p instanceof Expression) {
				definitions.put((VariableName)var_p, (Expression)val_p);
				return new ProgramValue(definitions);
			} else if (val_p instanceof ExpressionList) {
				throw new Exception("Value " + val_p + " must be of type expression.");	
			} 
		} else if (var_p instanceof VariableNameList) {	
			if (val_p instanceof ExpressionList) {	
				Iterator<VariableName> var = ((VariableNameList) var_p).getList().iterator();
				Iterator<Expression> exp = ((ExpressionList)val_p).getList().iterator();				
				while (var.hasNext() && exp.hasNext()) definitions.put(var.next(), exp.next());
				return new ProgramValue(definitions);
				
			} else if (val_p instanceof Expression) {
				throw new Exception("Value " + val_p + " must be of type list.");				
			}
		} 
		
		return new ProgramEquation(var_p, val_p);
	}

	@Override
	public String toString() {
		return var + "=" + val;
	}
}
