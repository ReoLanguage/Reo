package nl.cwi.reo.seepageautomata;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import nl.cwi.reo.automata.Automaton;
import nl.cwi.reo.semantics.Port;
import nl.cwi.reo.semantics.Semantics;
import nl.cwi.reo.semantics.SemanticsType;

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
		return 	SemanticsType.SA;
	}
	
	@Override
	public SeepageAutomaton getNode(SortedSet<Port> node) {
		return new SeepageAutomaton(super.getNode(node));
	}

	@Override
	public SeepageAutomaton rename(Map<Port, Port> links) {
		return new SeepageAutomaton(super.rename(links));
	}

	@Override
	public SeepageAutomaton evaluate(Map<String, String> params) {
		return new SeepageAutomaton(super.evaluate(params));
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
