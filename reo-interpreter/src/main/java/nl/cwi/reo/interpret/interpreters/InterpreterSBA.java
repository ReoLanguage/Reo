package nl.cwi.reo.interpret.interpreters;

import java.util.List;

import nl.cwi.reo.interpret.listeners.ListenerSBA;
import nl.cwi.reo.semantics.SemanticsType;
import nl.cwi.reo.semantics.symbolicautomata.SymbolicAutomaton;
import nl.cwi.reo.util.Monitor;

public class InterpreterSBA extends Interpreter<SymbolicAutomaton> {
	
	/**
	 * Constructs a Reo interpreter for symbolic automaton semantics.
	 * @param dirs		list of directories of Reo components
	 * @param values	parameter values of main component
	 * @param monitor	message container
	 */
	public InterpreterSBA(List<String> dirs, List<String> values, Monitor monitor) {
		super(SemanticsType.SBA, new ListenerSBA(monitor), dirs, values, monitor);	
	}	
}
