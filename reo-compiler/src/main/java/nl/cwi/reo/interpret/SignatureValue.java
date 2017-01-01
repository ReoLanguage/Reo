package nl.cwi.reo.interpret;

import java.util.Map;

public class SignatureValue {
	
	private DefinitionList params;
	
	private Map<NodeName, NodeName> iface;
	
	public SignatureValue(DefinitionList params, Map<NodeName, NodeName> iface) {
		this.params = params;
		this.iface = iface;
	}
	
	public DefinitionList getParameters() {
		return this.params;
	}
	
	public Map<NodeName, NodeName> getInterface() {
		return this.iface;
	}

}
