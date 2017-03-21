package nl.cwi.reo.semantics.rulebasedautomata;

import java.util.Map;

import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.ports.PortType;

public class PortVariable implements DataTerm {
	
	private final Port p;
	
	public PortVariable(Port p) {
		this.p = p;
	}

	public Port getPort() {
		return p;
	}

	@Override
	public boolean hadOutputs() {
		return p.getType() == PortType.OUT;
	}

	@Override
	public DataTerm rename(Map<Port, Port> links) {
		Port b = links.get(p);
		if (b != null)
			return new PortVariable(b);
		return this;
	}
}
