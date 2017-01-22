package nl.cwi.reo.interpret.signatures;

import java.util.Map;

import nl.cwi.reo.errors.CompilationException;
import nl.cwi.reo.interpret.Evaluable;
import nl.cwi.reo.interpret.ranges.Expression;
import nl.cwi.reo.interpret.ranges.Range;
import nl.cwi.reo.interpret.variables.Variable;
import nl.cwi.reo.interpret.variables.VariableName;

public class InterfaceNode implements Evaluable<InterfaceNode> {
	
	private final Variable var;
	
	private final PrioType prio;
	
	public InterfaceNode(Variable var, PrioType prio) {
		this.var = var;
		this.prio = prio;
	}
	
	public Variable getVariable() {
		return var;
	}

	@Override
	public InterfaceNode evaluate(Map<VariableName, Expression> params) throws CompilationException {
		Range rng = var.evaluate(params);
		if (rng instanceof Variable)
			return new InterfaceNode((Variable)rng, prio);
		return this;
	}
	
	@Override
	public String toString() {
		return "" + prio + var;
	}
}
