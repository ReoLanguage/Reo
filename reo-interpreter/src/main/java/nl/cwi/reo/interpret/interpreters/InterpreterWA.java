package nl.cwi.reo.interpret.interpreters;

import java.util.List;

import nl.cwi.reo.interpret.listeners.ListenerWA;
import nl.cwi.reo.semantics.SemanticsType;
import nl.cwi.reo.semantics.workautomata.WorkAutomaton;
import nl.cwi.reo.util.Monitor;

public class InterpreterWA extends Interpreter<WorkAutomaton> {
	
	/**
	 * Constructs a Reo interpreter for Work Automaton semantics.
	 * @param dirs		list of directories of Reo components
	 * @param values	parameter values of main component
	 * @param monitor	message container
	 */
	public InterpreterWA(List<String> dirs, List<String> values, Monitor monitor) {
		super(SemanticsType.WA, new ListenerWA(), dirs, values, monitor);	
	}	
}
