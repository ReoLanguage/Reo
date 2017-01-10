package nl.cwi.reo.interpret;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class Interface implements Evaluable<Interface> {
	
	private List<Variable> vars;
	
	public Interface(List<Variable> vars) {
		if (vars == null)
			throw new NullPointerException();
		this.vars = vars;
	}
	
	public List<Variable> getList() {
		return vars;
	}

	@Override
	public Interface evaluate(Map<VariableName, Expression> params) throws Exception {
		List<Variable> vars_p = new ArrayList<Variable>();
		for (Variable x : vars) {
			Expression x_p = x.evaluate(params);
			if (!(x_p instanceof Variable))
				x_p = x;
			vars_p.add((Variable)x_p);
		}
		return new Interface(vars_p);
	}
	
	@Override
	public String toString() {
		String s = "(";
		Iterator<Variable> var = vars.iterator();
		while (var.hasNext())
			s += var.next() + (var.hasNext() ? "," : "");
		return s + ")";
	}
}
