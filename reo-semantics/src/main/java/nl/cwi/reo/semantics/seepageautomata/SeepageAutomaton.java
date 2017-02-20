package nl.cwi.reo.semantics.seepageautomata;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.connectors.Semantics;
import nl.cwi.reo.interpret.connectors.SemanticsType;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.automata.Automaton;
import nl.cwi.reo.util.Monitor;

public class SeepageAutomaton extends Automaton<SeepageFunction> implements Semantics<SeepageAutomaton> {

	private static SeepageFunction label = new SeepageFunction();
	
	public SeepageAutomaton() {
		super(label);
	}
	
	private SeepageAutomaton(Automaton<SeepageFunction> A) {
		super(A.getStates(), A.getInterface(), A.getTransitions(), A.getInitial(), label);
	}

	@Override
	public SemanticsType getType() {
		return SemanticsType.SA;
	}
	
	@Override
	public SeepageAutomaton getNode(Set<Port> node) {
		return new SeepageAutomaton(super.getNode(node));
	}

	@Override
	public SeepageAutomaton rename(Map<Port, Port> links) {
		return new SeepageAutomaton(super.rename(links));
	}

	@Override
	public SeepageAutomaton evaluate(Scope s, Monitor m) {
		return new SeepageAutomaton(super.evaluate(s, m));
	}

	@Override
	public SeepageAutomaton compose(List<SeepageAutomaton> automata) {
		return new SeepageAutomaton(super.compose(automata));
	}

	@Override
	public SeepageAutomaton restrict(Collection<? extends Port> intface) {
		return new SeepageAutomaton(super.restrict(intface));
	}
}
