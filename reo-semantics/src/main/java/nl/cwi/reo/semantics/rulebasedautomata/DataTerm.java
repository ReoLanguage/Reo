package nl.cwi.reo.semantics.rulebasedautomata;

import java.util.Map;

import nl.cwi.reo.interpret.ports.Port;

public interface DataTerm {
	
	public boolean hadOutputs();

	public DataTerm rename(Map<Port, Port> links);
}
