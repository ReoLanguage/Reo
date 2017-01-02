package nl.cwi.reo.interpret;

import java.util.Collections;
import java.util.List;

public final class VariableNameList implements Variable {
	
	private final List<VariableName> list;
	
	public VariableNameList(List<VariableName> list) {
		this.list = Collections.unmodifiableList(list);
	}

	public List<VariableName> getList() {
		return list;
	}

	@Override
	public VariableNameList evaluate(DefinitionList params) throws Exception {
		return this;
	}	
}
