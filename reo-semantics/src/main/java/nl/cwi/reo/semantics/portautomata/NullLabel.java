package nl.cwi.reo.semantics.portautomata;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.automata.Label;
import nl.cwi.reo.util.Monitor;

public class NullLabel implements Label<NullLabel> {

	@Override
	public NullLabel compose(List<NullLabel> lbls) {
		return new NullLabel();
	}
	
	@Override
	public NullLabel restrict(Collection<? extends Port> intface) {
		return new NullLabel();
	}

	@Override
	public NullLabel rename(Map<Port, Port> links) {
		return new NullLabel();
	}

	@Override
	public NullLabel getLabel(Set<Port> N) {
		return new NullLabel();
	}

	@Override
	public NullLabel evaluate(Scope s, Monitor m) {
		return new NullLabel();
	}

	@Override
	public String toString() {
		return "-";
	}
}
