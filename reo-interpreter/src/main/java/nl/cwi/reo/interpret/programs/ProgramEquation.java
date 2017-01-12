package nl.cwi.reo.interpret.programs;

import java.util.Iterator;
import java.util.Map;

import nl.cwi.reo.interpret.arrays.Array;
import nl.cwi.reo.interpret.arrays.Expression;
import nl.cwi.reo.interpret.arrays.ExpressionRange;
import nl.cwi.reo.interpret.semantics.Definitions;
import nl.cwi.reo.interpret.semantics.InstanceList;
import nl.cwi.reo.interpret.variables.Variable;
import nl.cwi.reo.interpret.variables.VariableName;
import nl.cwi.reo.interpret.variables.VariableNameList;

public final class ProgramEquation implements ProgramExpression {

	private final Variable var;
	
	private final Array val;
	
	public ProgramEquation(Variable var, Array val) {
		if (var == null || val == null)
			throw new NullPointerException();
		this.var = var;
		this.val = val;
	}

	@Override
	public ProgramExpression evaluate(Map<VariableName, Expression> params) throws Exception {
		
		ProgramExpression prog = null;

		Array e = var.evaluate(params);
		if (!(e instanceof Variable))
			e = var;
		Variable var_p = (Variable)e;
		Array val_p = val.evaluate(params);
		
		if (var_p instanceof VariableName) {
			if (val_p instanceof Expression) {
				Definitions definitions = new Definitions();
				definitions.put((VariableName)var_p, (Expression)val_p);
				prog = new ProgramValue(definitions, new InstanceList());
			} else if (val_p instanceof ExpressionRange) {
				throw new Exception("Value " + val_p + " must be of type expression.");	
			} 
		} else if (var_p instanceof VariableNameList) {	
			if (val_p instanceof ExpressionRange) {	
				Definitions definitions = new Definitions();
				Iterator<VariableName> var = ((VariableNameList) var_p).getList().iterator();
				Iterator<Expression> exp = ((ExpressionRange)val_p).iterator();				
				while (var.hasNext() && exp.hasNext()) definitions.put(var.next(), exp.next());
				prog = new ProgramValue(definitions, new InstanceList());
				
			} else if (val_p instanceof Expression) {
				throw new Exception("Value " + val_p + " must be of type list.");				
			}
		} else {
			prog = new ProgramEquation(var_p, val_p);
		}
		
		return prog;
	}

	@Override
	public String toString() {
		return var + "=" + val;
	}
}
