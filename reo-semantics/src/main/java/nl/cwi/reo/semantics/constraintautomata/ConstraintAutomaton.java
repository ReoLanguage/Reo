package nl.cwi.reo.semantics.constraintautomata;

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

public class ConstraintAutomaton extends Automaton<CAMLabel> implements Semantics<ConstraintAutomaton> {
	
	private static CAMLabel label = new CAMLabel();
	
	public ConstraintAutomaton() {
		super(label);
	}
	
	public ConstraintAutomaton(SortedSet<State> Q, SortedSet<Port> P, Map<State, Set<Transition<CAMLabel>>> T, State q0) {
		super(Q, P, T, q0, label);
	}
	
	private ConstraintAutomaton(Automaton<CAMLabel> A) {
		super(A.getStates(), A.getInterface(), A.getTransitions(), A.getInitial(), label);
	}

	@Override
	public SemanticsType getType() {
		return SemanticsType.CAM;
	}

	@Override
	public ConstraintAutomaton getNode(Set<Port> node) {
		return new ConstraintAutomaton(super.getNode(node));
	}

	@Override
	public ConstraintAutomaton rename(Map<Port, Port> links) {
		return new ConstraintAutomaton(super.rename(links));
	}

	@Override
	public ConstraintAutomaton evaluate(Scope s, Monitor m) {
		return new ConstraintAutomaton(super.evaluate(s, m));
	}

	@Override
	public ConstraintAutomaton compose(List<ConstraintAutomaton> automata) {
		return new ConstraintAutomaton(super.compose(automata));
	}

	@Override
	public ConstraintAutomaton restrict(Collection<? extends Port> intface) {
		return new ConstraintAutomaton(super.restrict(intface));
	}
}
