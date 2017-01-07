package nl.cwi.reo.interpret;

import java.util.Map;

import nl.cwi.reo.semantics.Port;

public class SignatureInstance {
	
	private Map<VariableName, Expression> defs;
	
	private Map<Port, Port> links;
	
	public SignatureInstance(Map<VariableName, Expression> defs, Map<Port, Port> links) {
		if (defs == null || links == null)
			throw new IllegalArgumentException("Arguments cannot be null.");
		this.defs = defs;
		this.links = links;
	}
	
	public Map<VariableName, Expression> getDefinitions() {
		return this.defs;
	}
	
	public Map<Port, Port> getLinks() {
		return this.links;
	}

	@Override
	public String toString() {
		return defs + "" + links;
	}
}
