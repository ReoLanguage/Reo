package nl.cwi.reo.interpret;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class ParameterRange implements Parameter, Sequence {
	
	private final VariableRange var;
	
	private final ParameterType type;
	
	public ParameterRange(VariableRange var, ParameterType type) {
		this.var = var;
		this.type = type;
	}

	public VariableRange getVariable() {
		return var;
	}
	
	@Override
	public Parameter evaluate(DefinitionList params) throws Exception {
		
		Variable v = var.evaluate(params);
		
		if (v instanceof VariableList) {			
			List<ParameterName> param_list = new ArrayList<ParameterName>();
			for (Variable val : ((VariableList)v).getList()) {
				if (val instanceof VariableName)
					param_list.add(new ParameterName(((VariableName)val).getName(), this.type));
			}
			return new ParameterList(param_list);
		} else if (v instanceof VariableName) {
			return new ParameterName(((VariableName)v).getName(), this.type);			
		} 
		
		return this;
	}
	
	@Override
	public boolean equals(Object other) {
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof ParameterRange)) return false;
	    ParameterRange p = (ParameterRange)other;
	   	return Objects.equals(this.var, p.var) && Objects.equals(this.type, p.type);
	}
	
	@Override
	public int hashCode() {
	    return Objects.hash(this.var, this.type);
	}
}
