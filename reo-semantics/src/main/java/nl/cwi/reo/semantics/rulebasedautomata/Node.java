package nl.cwi.reo.semantics.rulebasedautomata;

import java.util.Map;

import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.ports.PortType;

public class Node implements DataTerm {
	
	private final Port p;
	
	public Node(Port p) {
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
			return new Node(b);
		return this;
	}
}
