package nl.cwi.reo.pr.autom;

import nl.cwi.reo.pr.autom.AutomatonFactory.Automaton;
import nl.cwi.reo.pr.autom.TransitionFactory.TransitionSet;
import nl.cwi.reo.pr.misc.IdObjectSpec;

public class StateSpec implements IdObjectSpec {
	private final Automaton automaton;
	private final boolean isInitial;
	private final TransitionSet transitions;

	//
	// CONSTRUCTORS
	//

	public StateSpec(Automaton automaton, boolean isInitial, TransitionSet transitions) {
		if (automaton == null)
			throw new NullPointerException();
		if (transitions == null)
			throw new NullPointerException();

		this.automaton = automaton;
		this.isInitial = isInitial;
		this.transitions = transitions;
	}

	//
	// METHODS - PUBLIC
	//
	
	public Automaton getAutomaton() {
		return automaton;
	}

	public TransitionSet getTransitions() {
		return transitions;
	}

	public boolean isInitial() {
		return isInitial;
	}
}