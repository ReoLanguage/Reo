package nl.cwi.pr.autom;

import java.util.Collections;
import java.util.Map;

import nl.cwi.pr.autom.AutomatonFactory.Automaton;
import nl.cwi.pr.autom.ConstraintFactory.Constraint;
import nl.cwi.pr.autom.StateFactory.State;
import nl.cwi.pr.misc.IdObjectSpec;
import nl.cwi.pr.misc.PortFactory.Port;
import nl.cwi.pr.misc.PortFactory.PortSet;

public class TransitionSpec implements IdObjectSpec {
	private final Automaton automaton;
	private final State source;
	private final State target;
	private final PortSet ports;
	private final Constraint constraint;
	private final Map<Integer, Port> representatives;

	//
	// CONSTRUCTORS
	//

	public TransitionSpec(Automaton automaton, State source, State target,
			PortSet ports, Constraint constraint) {

		if (automaton == null)
			throw new NullPointerException();
		if (source == null)
			throw new NullPointerException();
		if (target == null)
			throw new NullPointerException();
		if (ports == null)
			throw new NullPointerException();
		if (constraint == null)
			throw new NullPointerException();

		this.automaton = automaton;
		this.source = source;
		this.target = target;
		this.ports = ports;
		this.constraint = constraint;
		this.representatives = Collections.emptyMap();
	}

	public TransitionSpec(Automaton automaton, State source, State target,
			PortSet ports, Constraint constraint,
			Map<Integer, Port> representatives) {

		if (automaton == null)
			throw new NullPointerException();
		if (source == null)
			throw new NullPointerException();
		if (target == null)
			throw new NullPointerException();
		if (ports == null)
			throw new NullPointerException();
		if (constraint == null)
			throw new NullPointerException();
		if (representatives == null)
			throw new NullPointerException();
		if (representatives.containsKey(null))
			throw new NullPointerException();
		if (representatives.containsValue(null))
			throw new NullPointerException();

		if (representatives.isEmpty())
			throw new IllegalArgumentException();

		this.automaton = automaton;
		this.source = source;
		this.target = target;
		this.ports = ports;
		this.constraint = constraint;
		this.representatives = representatives;
	}

	//
	// METHODS - PUBLIC
	//

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			throw new NullPointerException();

		return obj instanceof TransitionSpec && equals((TransitionSpec) obj);
	}

	public boolean equals(TransitionSpec spec) {
		if (spec == null)
			throw new NullPointerException();

		return automaton.equals(spec.automaton) && source.equals(spec.source)
				&& target.equals(spec.target) && ports.equals(spec.ports)
				&& constraint.equals(spec.constraint)
				&& representatives.equals(spec.representatives);
	}

	public Automaton getAutomaton() {
		return automaton;
	}

	public Constraint getConstraint() {
		return constraint;
	}

	public Map<Integer, Port> getRepresentatives() {
		return representatives;
	}

	public PortSet getPorts() {
		return ports;
	}

	public State getSource() {
		return source;
	}

	public State getTarget() {
		return target;
	}

	@Override
	public int hashCode() {
		return source.hashCode() + target.hashCode();
	}
}