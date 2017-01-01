package nl.cwi.reo.interpret;

import java.util.Collections;
import java.util.List;

public final class ParameterList implements Parameter, Sequence {
	
	private final List<ParameterName> params;
	
	public ParameterList(List<ParameterName> params) {
		this.params = Collections.unmodifiableList(params);
	}
	
	public List<ParameterName> getList() {
		return params;
	}

	@Override
	public Parameter evaluate(DefinitionList params)
			throws Exception {
		return this;
	}

}
