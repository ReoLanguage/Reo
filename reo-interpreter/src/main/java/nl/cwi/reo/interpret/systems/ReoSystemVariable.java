package nl.cwi.reo.interpret.systems;

import java.util.Map;

import nl.cwi.reo.interpret.blocks.ReoBlock;
import nl.cwi.reo.interpret.expressions.ValueList;
import nl.cwi.reo.interpret.variables.Variable;
import nl.cwi.reo.interpret.variables.VariableNameList;
import nl.cwi.reo.semantics.api.Expression;
import nl.cwi.reo.semantics.api.Semantics;

public class ReoSystemVariable<T extends Semantics<T>> implements ReoSystem<T> {
	
	private Variable var;
	
	public ReoSystemVariable(Variable var) {
		if (var == null)
			throw new NullPointerException();
		this.var = var;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReoBlock<T> instantiate(ValueList values, VariableNameList iface) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ReoSystem<T> evaluate(Map<String, Expression> params) {
		Expression e = var.evaluate(params);
		if (e instanceof ReoSystem)
			return (ReoSystem<T>)e;
		else if (e instanceof Variable)
			return new ReoSystemVariable<T>((Variable)e);
		return this;	
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "" + var;
	}
}
