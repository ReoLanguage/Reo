package nl.cwi.pr.autom.libr;

import java.util.ArrayList;
import java.util.List;

import nl.cwi.pr.autom.UserDefinedAutomaton;
import nl.cwi.pr.autom.UserDefinedInitializer;
import nl.cwi.pr.autom.ConstraintFactory.Constraint;
import nl.cwi.pr.autom.LiteralFactory.Literal;
import nl.cwi.pr.autom.StateFactory.State;
import nl.cwi.pr.misc.PortFactory.Port;

public class Node extends UserDefinedInitializer {

	@Override
	public boolean canInitialize(UserDefinedAutomaton automaton) {
		if (automaton == null)
			throw new NullPointerException();

		return automaton.countInputPorts() >= 0
				&& automaton.countOutputPorts() >= 0
				&& automaton.countExtralogicals() == 0;
	}

	@Override
	public void initialize(UserDefinedAutomaton automaton) {
		if (automaton == null)
			throw new NullPointerException();

		Port[] inputPorts = automaton.getInputPorts();
		Port[] outputPorts = automaton.getOutputPorts();
		// Extralogical[] extralogicals = automaton.getExtralogicals();

		State state = automaton.addThenGetState(true);

		List<Port> ports;
		Constraint constraint;

		List<Literal> literals;

		for (Port p1 : inputPorts) {
			literals = new ArrayList<>();
			ports = new ArrayList<>();
			ports.add(p1);

			for (Port p2 : outputPorts) {
				ports.add(p2);
				literals.add(automaton.newLiteral(true, automaton.newTerm(p1),
						automaton.newTerm(p2)));
			}

			constraint = automaton.newConstraint(literals);
			automaton.addOrKeepTransition(state, state, ports, constraint);
		}
	}
}
