package nl.cwi.reo.interpret;

import java.util.Map;

public class SignatureInstance {
	
	private DefinitionList defs;
	
	private Map<Node, Node> links;
	
	public SignatureInstance(DefinitionList defs, Map<Node, Node> links) {
		this.defs = defs;
		this.links = links;
	}
	
	public DefinitionList getDefinitions() {
		return this.defs;
	}
	
	public Map<Node, Node> getLinks() {
		return this.links;
	}

}
