package nl.cwi.reo.interpret;

import java.util.ArrayList;
import java.util.List;

public final class Interface implements Evaluable<Interface> {
	
	private List<Variable> vars;
	
	public Interface(List<Variable> vars) {
		this.vars = vars;
	}
	
	public List<Variable> getList() {
		return vars;
	}

	@Override
	public Interface evaluate(DefinitionList params) throws Exception {
		List<Variable> vars_p = new ArrayList<Variable>();
		for (Variable x : vars) 
			vars_p.add(x.evaluate(params));
		return new Interface(vars_p);
	}	
}
