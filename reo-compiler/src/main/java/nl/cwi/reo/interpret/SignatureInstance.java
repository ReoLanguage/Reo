package nl.cwi.reo.interpret;

import java.util.Map;

public class SignatureInstance {
	
	private DefinitionList defs;
	
	private Map<Port, Port> links;
	
	public SignatureInstance(DefinitionList defs, Map<Port, Port> links) {
		if (defs == null || links == null)
			throw new IllegalArgumentException("Arguments cannot be null.");
		this.defs = defs;
		this.links = links;
	}
	
	public DefinitionList getDefinitions() {
		return this.defs;
	}
	
	public Map<Port, Port> getLinks() {
		return this.links;
	}

}
