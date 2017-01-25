package nl.cwi.reo.portautomata;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.cwi.reo.automata.Label;
import nl.cwi.reo.semantics.api.Port;

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
	public NullLabel evaluate(Map<String, String> params) {
		return new NullLabel();
	}

	@Override
	public String toString() {
		return "-";
	}
}
