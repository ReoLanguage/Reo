package nl.cwi.reo.semantics.symbolicautomata;

import java.util.Map;

import nl.cwi.reo.interpret.ports.Port;

public class Constant implements Term {
	
	public static final boolean constant = true;
	
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

	public String toString(){
		if(c!=null)
			return c.toString();
		return "null";
	}
	
	@Override
	public Term rename(Map<Port, Port> links) {
		return this;
	}
}
