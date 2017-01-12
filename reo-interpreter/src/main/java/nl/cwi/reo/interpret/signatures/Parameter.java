package nl.cwi.reo.interpret.signatures;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nl.cwi.reo.interpret.Evaluable;
import nl.cwi.reo.interpret.arrays.Array;
import nl.cwi.reo.interpret.arrays.Expression;
import nl.cwi.reo.interpret.variables.Variable;
import nl.cwi.reo.interpret.variables.VariableName;
import nl.cwi.reo.interpret.variables.VariableNameList;

/**
 * An immutable parameter implementation.
 */
public final class Parameter implements Evaluable<Parameter> {
	
	private final Variable var;
	
	private final ParameterType type;
	
	public Parameter(Variable var, ParameterType type) {
		if (var == null || type == null)
			throw new NullPointerException();
		this.var = var;
		this.type = type;
	}

	public Variable getVariable() {
		return this.var;
	}
	
	public ParameterType getType() {
		return this.type;
	}
	
	public List<Parameter> getList() {
		List<Parameter> params = null;
		if (var instanceof VariableNameList) {
			params = new ArrayList<Parameter>();
			for (VariableName v : ((VariableNameList)var).getList())
				params.add(new Parameter(v, type));
		}
		return params;
	}

	@Override
	public Parameter evaluate(Map<VariableName, Expression> params) throws Exception {
		Array e = var.evaluate(params);
		if (!(e instanceof Variable))
			e = var;
		return new Parameter((Variable)e, type);
	}
	
	@Override
	public String toString() {
		return var + (type instanceof TypeTag ? ":" : "") + type;
	}
}
