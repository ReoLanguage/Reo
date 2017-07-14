package nl.cwi.reo.semantics.seepageautomata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.List;
import java.util.Set;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.automata.Label;
import nl.cwi.reo.util.Monitor;

public class SeepageFunction implements Label<SeepageFunction> {
	
	public final List<Map<String,Set<Set<String>>>> equations;
	
	public SeepageFunction() {
		this.equations = new ArrayList<Map<String,Set<Set<String>>>>();
	}
	
	public SeepageFunction(List<Map<String,Set<Set<String>>>> eqs) {
		this.equations = eqs;
	}
	
	public SeepageFunction(SeepageFunction S) {
		this.equations = S.equations;
	}

	@Override
	public SeepageFunction compose(List<SeepageFunction> lbls) {
		List<Map<String,Set<Set<String>>>> eqs_all = new ArrayList<Map<String,Set<Set<String>>>>();
		for (SeepageFunction F : lbls)
			eqs_all.addAll(F.equations);
		return new SeepageFunction(eqs_all);
	}

	@Override
	public SeepageFunction restrict(Collection<? extends Port> intface) {
		return new SeepageFunction(this);
	}

	@Override
	public SeepageFunction rename(Map<Port, Port> links) {
		return this;
	}

	@Override
	public SeepageFunction getLabel(Set<Port> N) {
		return this;
	}

	@Override
	public SeepageFunction evaluate(Scope s, Monitor m) {
		return this;
	}
}
