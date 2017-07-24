package nl.cwi.reo.semantics.portautomata;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.SemanticsType;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.Semantics;
import nl.cwi.reo.semantics.automata.Automaton;
import nl.cwi.reo.semantics.automata.State;
import nl.cwi.reo.semantics.automata.Transition;
import nl.cwi.reo.util.Monitor;

// TODO: Auto-generated Javadoc
/**
 * The Class PortAutomaton.
 */
public class PortAutomaton extends Automaton<NullLabel> implements Semantics<PortAutomaton> {

	/** The label. */
	private static NullLabel label = new NullLabel();

	/**
	 * Instantiates a new port automaton.
	 */
	public PortAutomaton() {
		super(label);
	}

	/**
	 * Instantiates a new port automaton.
	 *
	 * @param Q
	 *            the q
	 * @param P
	 *            the p
	 * @param T
	 *            the t
	 * @param q0
	 *            the q 0
	 */
	public PortAutomaton(SortedSet<State> Q, SortedSet<Port> P, Map<State, Set<Transition<NullLabel>>> T, State q0) {
		super(Q, P, T, q0, label);
	}

	/**
	 * Instantiates a new port automaton.
	 *
	 * @param A
	 *            the a
	 */
	private PortAutomaton(Automaton<NullLabel> A) {
		super(A.getStates(), A.getInterface(), A.getTransitions(), A.getInitial(), label);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.semantics.Semantics#getType()
	 */
	@Override
	public SemanticsType getType() {
		return SemanticsType.PA;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.semantics.automata.Automaton#getNode(java.util.Set)
	 */
	@Override
	public PortAutomaton getNode(Set<Port> node) {
		return new PortAutomaton(super.getNode(node));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.semantics.automata.Automaton#rename(java.util.Map)
	 */
	@Override
	public PortAutomaton rename(Map<Port, Port> links) {
		return new PortAutomaton(super.rename(links));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.semantics.Semantics#compose(java.util.List)
	 */
	@Override
	public PortAutomaton compose(List<PortAutomaton> automata) {
		return new PortAutomaton(super.compose(automata));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.semantics.automata.Automaton#restrict(java.util.Collection)
	 */
	@Override
	public PortAutomaton restrict(Collection<? extends Port> intface) {
		return new PortAutomaton(super.restrict(intface));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.semantics.automata.Automaton#evaluate(nl.cwi.reo.interpret.
	 * Scope, nl.cwi.reo.util.Monitor)
	 */
	@Override
	public PortAutomaton evaluate(Scope s, Monitor m) {
		return new PortAutomaton(super.evaluate(s, m));
	}

	@Override
	public PortAutomaton getDefault(Set<Port> iface) {
		// TODO Auto-generated method stub
		return null;
	}
}
