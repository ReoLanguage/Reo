package nl.cwi.reo.semantics.rulebasedautomata;

import java.util.Map;

import nl.cwi.reo.interpret.ports.Port;

public class MemoryCell implements DataTerm {
	
	private final int k;
	
	public MemoryCell(int k) {
		this.k = k;
	}

	public String getName() {
		return "q" + k;
	}

	@Override
	public boolean hadOutputs() {
		return false;
	}

	@Override
	public DataTerm rename(Map<Port, Port> links) {
		return this;
	}
}
