package nl.cwi.pr.autom.libr;

import java.util.Arrays;
import java.util.List;

import nl.cwi.pr.autom.UserDefinedAutomaton;
import nl.cwi.pr.autom.UserDefinedInitializer;
import nl.cwi.pr.autom.ConstraintFactory.Constraint;
import nl.cwi.pr.autom.MemoryCellFactory.MemoryCell;
import nl.cwi.pr.autom.StateFactory.State;
import nl.cwi.pr.misc.PortFactory.Port;

public class Fifo extends UserDefinedInitializer {

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

		State state1 = automaton.addThenGetState(true);
		State state2 = automaton.addThenGetState(false);

		MemoryCell memoryCell = automaton.addThenGetMemoryCell();

		List<Port> ports;
		Constraint constraint;

		ports = Arrays.asList(inputPorts[0]);
		constraint = automaton.newConstraint(Arrays.asList(automaton
				.newLiteral(true, automaton.newTerm(inputPorts[0]),
						automaton.newTerm(memoryCell, true))));
		automaton.addOrKeepTransition(state1, state2, ports, constraint);

		ports = Arrays.asList(outputPorts[0]);
		constraint = automaton.newConstraint(Arrays.asList(automaton
				.newLiteral(true, automaton.newTerm(outputPorts[0]),
						automaton.newTerm(memoryCell, false))));
		automaton.addOrKeepTransition(state2, state1, ports, constraint);
	}
}
