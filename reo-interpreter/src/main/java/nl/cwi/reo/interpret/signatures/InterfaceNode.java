package nl.cwi.reo.interpret.signatures;

import java.util.Map;

import nl.cwi.reo.errors.CompilationException;
import nl.cwi.reo.interpret.variables.Variable;
import nl.cwi.reo.semantics.api.Evaluable;
import nl.cwi.reo.semantics.api.Expression;
import nl.cwi.reo.semantics.api.PrioType;

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
	public InterfaceNode evaluate(Map<String, Expression> params) throws CompilationException {
		Expression rng = var.evaluate(params);
		if (rng instanceof Variable)
			return new InterfaceNode((Variable)rng, prio);
		return this;
	}
	
	@Override
	public String toString() {
		return "" + prio + var;
	}
}
