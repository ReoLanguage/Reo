package nl.cwi.reo.semantics.symbolicautomata;

import java.util.Map;

import nl.cwi.reo.interpret.ports.Port;

public interface Term {
	
	public boolean hadOutputs();

	public Term rename(Map<Port, Port> links);
}
