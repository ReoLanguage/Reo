package nl.cwi.reo.semantics.imperativeautomata;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.Semantics;
import nl.cwi.reo.semantics.SemanticsType;
import nl.cwi.reo.semantics.automata.Automaton;
import nl.cwi.reo.semantics.automata.State;
import nl.cwi.reo.semantics.automata.Transition;
import nl.cwi.reo.util.Monitor;

public class ImperativeAutomaton extends Automaton<IALabel> implements Semantics<ImperativeAutomaton> {
	
	private static IALabel label = new IALabel();
	
	public ImperativeAutomaton() {
		super(label);
	}
	
	public ImperativeAutomaton(SortedSet<State> Q, SortedSet<Port> P, Map<State, Set<Transition<IALabel>>> T, State q0) {
		super(Q, P, T, q0, label);
	}
	
	private ImperativeAutomaton(Automaton<IALabel> A) {
		super(A.getStates(), A.getInterface(), A.getTransitions(), A.getInitial(), label);
	}

	@Override
	public SemanticsType getType() {
		return SemanticsType.PA;
	}

	@Override
	public ImperativeAutomaton getNode(Set<Port> node) {
		return new ImperativeAutomaton(super.getNode(node));
	}

	@Override
	public ImperativeAutomaton rename(Map<Port, Port> links) {
		return new ImperativeAutomaton(super.rename(links));
	}

	@Override
	public ImperativeAutomaton evaluate(Scope s, Monitor m) {
		return new ImperativeAutomaton(super.evaluate(s, m));
	}

	@Override
	public ImperativeAutomaton compose(List<ImperativeAutomaton> automata) {
		return new ImperativeAutomaton(super.compose(automata));
	}

	@Override
	public ImperativeAutomaton restrict(Collection<? extends Port> intface) {
		return new ImperativeAutomaton(super.restrict(intface));
	}
}
