package nl.cwi.reo.semantics.rulebasedautomata;

import java.util.Map;

import nl.cwi.reo.interpret.ports.Port;

public class Constant implements DataTerm {
	
	private final Object c;
	
	public Constant(Object c) {
		this.c = c;
	}

	public Object getValue() {
		return c;
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
