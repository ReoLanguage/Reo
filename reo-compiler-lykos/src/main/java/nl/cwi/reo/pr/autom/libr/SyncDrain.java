package nl.cwi.reo.pr.autom.libr;

import java.util.Arrays;
import java.util.List;

import nl.cwi.reo.pr.autom.Extralogical;
import nl.cwi.reo.pr.autom.UserDefinedAutomaton;
import nl.cwi.reo.pr.autom.UserDefinedInitializer;
import nl.cwi.reo.pr.autom.ConstraintFactory.Constraint;
import nl.cwi.reo.pr.autom.LiteralFactory.Literal;
import nl.cwi.reo.pr.autom.StateFactory.State;
import nl.cwi.reo.pr.misc.TypedName;
import nl.cwi.reo.pr.misc.PortFactory.Port;
import nl.cwi.reo.pr.misc.TypedName.Type;

public class SyncDrain extends UserDefinedInitializer {

	@Override
	public boolean canInitialize(UserDefinedAutomaton automaton) {
		if (automaton == null)
			throw new NullPointerException();

		return automaton.countInputPorts() == 2
				&& automaton.countOutputPorts() == 0
				&& automaton.countExtralogicals() == 0;
	}

	@Override
	public void initialize(UserDefinedAutomaton automaton) {
		if (automaton == null)
			throw new NullPointerException();

		Port[] inputPorts = automaton.getInputPorts();
		// Port[] outputPorts = automaton.getOutputPorts();
		// Extralogical[] extralogicals = automaton.getExtralogicals();
		Extralogical e = new Extralogical("nl.cwi.pr.runtime.Relations.True");
		
		State state = automaton.addThenGetState(true);

		List<Port> ports;
		Constraint constraint;

		ports = Arrays.asList(inputPorts[0], inputPorts[1]);
		constraint = automaton.newConstraint(Arrays.asList(automaton
				.newLiteral(true, e, Arrays.asList(
						automaton.newTerm(inputPorts[0]),
						automaton.newTerm(inputPorts[1])))));
		automaton.addOrKeepTransition(state, state, ports, constraint);
		
//		State state = automaton.addThenGetState(true);

//		List<Port> ports;
//		Constraint constraint;

//		ports = Arrays.asList(inputPorts[0], inputPorts[1]);
//		constraint = automaton.newConstraint(Arrays.<Literal> asList());
//		automaton.addOrKeepTransition(state, state, ports, constraint);
	}
}
