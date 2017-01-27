package nl.cwi.reo.pr.autom.libr;

import java.util.Arrays;
import java.util.List;

import nl.cwi.reo.pr.autom.UserDefinedAutomaton;
import nl.cwi.reo.pr.autom.UserDefinedInitializer;
import nl.cwi.reo.pr.autom.ConstraintFactory.Constraint;
import nl.cwi.reo.pr.autom.StateFactory.State;
import nl.cwi.reo.pr.misc.PortFactory.Port;

public class Sync extends UserDefinedInitializer {

	@Override
	public boolean canInitialize(UserDefinedAutomaton automaton) {
		if (automaton == null)
			throw new NullPointerException();

		return automaton.countInputPorts() == 1
				&& automaton.countOutputPorts() == 1
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

		ports = Arrays.asList(inputPorts[0], outputPorts[0]);
		constraint = automaton.newConstraint(Arrays.asList(automaton
				.newLiteral(true, automaton.newTerm(inputPorts[0]),
						automaton.newTerm(outputPorts[0]))));
		automaton.addOrKeepTransition(state, state, ports, constraint);
	}
}
