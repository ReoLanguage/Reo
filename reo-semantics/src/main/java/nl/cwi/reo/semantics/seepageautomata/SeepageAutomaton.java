package nl.cwi.reo.semantics.seepageautomata;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.SemanticsType;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.semantics.Semantics;
import nl.cwi.reo.semantics.automata.Automaton;
import nl.cwi.reo.util.Monitor;

// TODO: Auto-generated Javadoc
/**
 * The Class SeepageAutomaton.
 */
public class SeepageAutomaton extends Automaton<SeepageFunction> implements Semantics<SeepageAutomaton> {

	/** The label. */
	private static SeepageFunction label = new SeepageFunction();

	/**
	 * Instantiates a new seepage automaton.
	 */
	public SeepageAutomaton() {
		super(label);
	}

	/**
	 * Instantiates a new seepage automaton.
	 *
	 * @param A
	 *            the a
	 */
	private SeepageAutomaton(Automaton<SeepageFunction> A) {
		super(A.getStates(), A.getInterface(), A.getTransitions(), A.getInitial(), label);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.semantics.Semantics#getType()
	 */
	@Override
	public SemanticsType getType() {
		return SemanticsType.SA;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.semantics.automata.Automaton#getNode(java.util.Set)
	 */
	@Override
	public SeepageAutomaton getNode(Set<Port> node) {
		return new SeepageAutomaton(super.getNode(node));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.semantics.automata.Automaton#rename(java.util.Map)
	 */
	@Override
	public SeepageAutomaton rename(Map<Port, Port> links) {
		return new SeepageAutomaton(super.rename(links));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.semantics.automata.Automaton#evaluate(nl.cwi.reo.interpret.
	 * Scope, nl.cwi.reo.util.Monitor)
	 */
	@Override
	public SeepageAutomaton evaluate(Scope s, Monitor m) {
		return new SeepageAutomaton(super.evaluate(s, m));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.semantics.Semantics#compose(java.util.List)
	 */
	@Override
	public SeepageAutomaton compose(List<SeepageAutomaton> automata) {
		return new SeepageAutomaton(super.compose(automata));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nl.cwi.reo.semantics.automata.Automaton#restrict(java.util.Collection)
	 */
	@Override
	public SeepageAutomaton restrict(Collection<? extends Port> intface) {
		return new SeepageAutomaton(super.restrict(intface));
	}

	@Override
	public SeepageAutomaton getDefault(Set<Port> iface) {
		// TODO Auto-generated method stub
		return null;
	}
}
