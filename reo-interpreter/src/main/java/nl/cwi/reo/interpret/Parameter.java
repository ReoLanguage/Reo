package nl.cwi.reo.interpret;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class Parameter implements Evaluable<Parameter> {
	
	private final Variable var;
	
	private final ParameterType type;
	
	public Parameter(Variable var, ParameterType type) {
		if (var == null || type == null)
			throw new IllegalArgumentException("Arguments cannot be null.");
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
		return new Parameter(var.evaluate(params), type);
	}
	
	@Override
	public String toString() {
		return var + (type instanceof TypeTag ? ":" : "") + type;
	}
}
