package nl.cwi.pr.autom.libr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.cwi.pr.autom.UserDefinedAutomaton;
import nl.cwi.pr.autom.UserDefinedInitializer;
import nl.cwi.pr.autom.ConstraintFactory.Constraint;
import nl.cwi.pr.autom.StateFactory.State;
import nl.cwi.pr.misc.PortFactory.Port;

public class XNode extends UserDefinedInitializer {

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

		for (Port p1 : inputPorts)
			for (Port p2 : outputPorts) {
				ports = new ArrayList<>();
				ports.add(p1);
				ports.add(p2);

				constraint = automaton.newConstraint(Arrays.asList(automaton
						.newLiteral(true, automaton.newTerm(p1),
								automaton.newTerm(p2))));

				automaton.addOrKeepTransition(state, state, ports, constraint);
			}
	}
}
