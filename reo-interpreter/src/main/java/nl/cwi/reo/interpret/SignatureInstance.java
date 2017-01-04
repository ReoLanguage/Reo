package nl.cwi.reo.interpret;

import java.util.Map;

import nl.cwi.reo.semantics.Port;

public class SignatureInstance {
	
	private BodyDefinitionList defs;
	
	private Map<Port, Port> links;
	
	public SignatureInstance(BodyDefinitionList defs, Map<Port, Port> links) {
		if (defs == null || links == null)
			throw new IllegalArgumentException("Arguments cannot be null.");
		this.defs = defs;
		this.links = links;
	}
	
	public BodyDefinitionList getDefinitions() {
		return this.defs;
	}
	
	public Map<Port, Port> getLinks() {
		return this.links;
	}

}
