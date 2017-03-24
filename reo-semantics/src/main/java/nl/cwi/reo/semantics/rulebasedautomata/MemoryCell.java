package nl.cwi.reo.semantics.rulebasedautomata;

import java.util.Map;

import nl.cwi.reo.interpret.ports.Port;

public class MemoryCell implements DataTerm {
	
	private final int k;
	
	private final boolean prime;
	
	public MemoryCell(int k, boolean prime) {
		this.k = k;
		this.prime = prime;
	}

	public String getName() {
		return "q" + k;
	}
	
	public boolean hasPrime() {
		return prime;
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
