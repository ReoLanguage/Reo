package nl.cwi.reo.interpret;

import java.util.List;

import nl.cwi.reo.interpret.listeners.ListenerWA;
import nl.cwi.reo.semantics.api.SemanticsType;
import nl.cwi.reo.workautomata.WorkAutomaton;

public class InterpreterWA extends Interpreter<WorkAutomaton> {
	
	/**
	 * Constructs a Reo interpreter for Work Automaton semantics.
	 * @param dirs		list of directories of Reo components
	 */
	public InterpreterWA(List<String> dirs, List<String> params) {
		super(SemanticsType.WA, new ListenerWA(), params, dirs);	
	}	
}
