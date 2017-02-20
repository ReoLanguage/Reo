package nl.cwi.reo.semantics.portautomata;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.connectors.Semantics;
import nl.cwi.reo.interpret.connectors.SemanticsType;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.automata.Automaton;
import nl.cwi.reo.semantics.automata.State;
import nl.cwi.reo.semantics.automata.Transition;
import nl.cwi.reo.util.Monitor;

public class PortAutomaton extends Automaton<NullLabel> implements Semantics<PortAutomaton> {
	
	private static NullLabel label = new NullLabel();
	

	public PortAutomaton() {
		super(label);
	}
	
	public PortAutomaton(SortedSet<State> Q, SortedSet<Port> P, Map<State, Set<Transition<NullLabel>>> T, State q0) {
		super(Q, P, T, q0, label);
	}
	
	private PortAutomaton(Automaton<NullLabel> A) {
		super(A.getStates(), A.getInterface(), A.getTransitions(), A.getInitial(), label);
	}

	@Override
	public SemanticsType getType() {
		return SemanticsType.PA;
	}

	@Override
	public PortAutomaton getNode(Set<Port> node) {
		return new PortAutomaton(super.getNode(node));
	}

	@Override
	public PortAutomaton rename(Map<Port, Port> links) {
		return new PortAutomaton(super.rename(links));
	}

	@Override
	public PortAutomaton compose(List<PortAutomaton> automata) {
		return new PortAutomaton(super.compose(automata));
	}

	@Override
	public PortAutomaton restrict(Collection<? extends Port> intface) {
		return new PortAutomaton(super.restrict(intface));
	}

	@Override
	public PortAutomaton evaluate(Scope s, Monitor m) {
		return new PortAutomaton(super.evaluate(s, m));
	}
}
