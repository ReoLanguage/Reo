package nl.cwi.reo.interpret;

import java.util.List;

import nl.cwi.reo.interpret.listeners.ListenerPA;
import nl.cwi.reo.portautomata.PortAutomaton;
import nl.cwi.reo.semantics.SemanticsType;

public class InterpreterPA extends Interpreter<PortAutomaton> {
	
	/**
	 * Constructs a Reo interpreter for Work Automaton semantics.
	 * @param dirs		list of directories of Reo components
	 */
	public InterpreterPA(List<String> dirs) {
		super(SemanticsType.PA, dirs, new ListenerPA());	
	}	
}
