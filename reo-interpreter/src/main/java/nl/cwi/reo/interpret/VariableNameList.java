package nl.cwi.reo.interpret;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class VariableNameList implements Variable {
	
	private final List<VariableName> list;
	
	public VariableNameList(List<VariableName> list) {
		if (list == null)
			throw new NullPointerException();
		this.list = Collections.unmodifiableList(list);
	}

	public List<VariableName> getList() {
		return list;
	}

	@Override
	public VariableNameList evaluate(Map<VariableName, Expression> params) throws Exception {
		return this;
	}
	
	@Override
	public String toString() {
		return "" + list;
	}
}
