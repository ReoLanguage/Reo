package nl.cwi.reo.interpret;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class VariableList implements Variable, Sequence {
	
	private final List<Variable> list;
	
	public VariableList(List<Variable> list) {
		if (list == null)
			throw new IllegalArgumentException("Argument cannot be null.");
		this.list = Collections.unmodifiableList(list);
	}

	public List<Variable> getList() {
		return list;
	}

	@Override
	public VariableList evaluate(DefinitionList params) throws Exception {
		List<Variable> list_p = new ArrayList<Variable>();
		for (Variable var : list) {
			Variable var_p = var.evaluate(params);
			if (var_p instanceof VariableList) {
				for (Variable x : ((VariableList)var_p).getList())
					list_p.add(x);
			} else {
				list_p.add(var_p);
			}
		}
		return new VariableList(list_p);
	}
	
	
}
