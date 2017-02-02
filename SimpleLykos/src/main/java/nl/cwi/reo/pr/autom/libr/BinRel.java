package nl.cwi.reo.pr.autom.libr;

import java.util.Arrays;
import java.util.List;

import nl.cwi.reo.pr.autom.Extralogical;
import nl.cwi.reo.pr.autom.UserDefinedAutomaton;
import nl.cwi.reo.pr.autom.UserDefinedInitializer;
import nl.cwi.reo.pr.autom.ConstraintFactory.Constraint;
import nl.cwi.reo.pr.autom.StateFactory.State;
import nl.cwi.reo.pr.misc.PortFactory.Port;

public class BinRel extends UserDefinedInitializer {

	@Override
	public boolean canInitialize(UserDefinedAutomaton automaton) {
		if (automaton == null)
			throw new NullPointerException();

		return automaton.countInputPorts() == 2
				&& automaton.countOutputPorts() == 0
				&& automaton.countExtralogicals() == 1;
	}

	@Override
	public void initialize(UserDefinedAutomaton automaton) {
		if (automaton == null)
			throw new NullPointerException();

		Port[] inputPorts = automaton.getInputPorts();
		// Port[] outputPorts = automaton.getOutputPorts();
		Extralogical[] extralogicals = automaton.getExtralogicals();

		State state = automaton.addThenGetState(true);

		List<Port> ports;
		Constraint constraint;

		ports = Arrays.asList(inputPorts[0], inputPorts[1]);
		constraint = automaton.newConstraint(Arrays.asList(automaton
				.newLiteral(true, extralogicals[0], Arrays.asList(
						automaton.newTerm(inputPorts[0]),
						automaton.newTerm(inputPorts[1])))));
		automaton.addOrKeepTransition(state, state, ports, constraint);
	}
}
