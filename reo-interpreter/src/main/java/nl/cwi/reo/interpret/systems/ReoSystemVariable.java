package nl.cwi.reo.interpret.systems;

import java.util.Map;

import nl.cwi.reo.interpret.ranges.Range;
import nl.cwi.reo.errors.CompilationException;
import nl.cwi.reo.interpret.blocks.ReoBlock;
import nl.cwi.reo.interpret.ranges.Expression;
import nl.cwi.reo.interpret.ranges.ExpressionList;
import nl.cwi.reo.interpret.variables.Variable;
import nl.cwi.reo.interpret.variables.VariableNameList;
import nl.cwi.reo.semantics.api.Semantics;

public class ReoSystemVariable<T extends Semantics<T>> implements ReoSystem<T> {
	
	private Variable var;
	
	public ReoSystemVariable(Variable var) {
		if (var == null)
			throw new NullPointerException();
		this.var = var;
	}

	@Override
	public ReoBlock<T> instantiate(ExpressionList values, VariableNameList iface) throws CompilationException {
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ReoSystem<T> evaluate(Map<String, Expression> params) throws CompilationException {
		Range e = var.evaluate(params);
		if (e instanceof ReoSystem) {
			return (ReoSystem<T>)e;
		} else if (e instanceof Variable) {
			return new ReoSystemVariable<T>((Variable)e);
		} 
		return this;	
	}
	
	@Override
	public String toString() {
		return "" + var;
	}
}
