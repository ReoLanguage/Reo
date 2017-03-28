package nl.cwi.reo.semantics.symbolicautomata;

import java.util.Map;

import nl.cwi.reo.interpret.ports.Port;

public class MemoryCell implements Variable {
	
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
	public Term rename(Map<Port, Port> links) {
		return this;
	}
}
