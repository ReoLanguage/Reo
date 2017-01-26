package nl.cwi.reo.interpret;

import java.util.List;

import nl.cwi.reo.interpret.listeners.ListenerPR;
import nl.cwi.reo.prautomata.PRAutomaton;
import nl.cwi.reo.semantics.SemanticsType;

public class InterpreterPR extends Interpreter<PRAutomaton> {
	
	/**
	 * Constructs a Reo interpreter for Port Automaton semantics.
	 * @param dirs		list of directories of Reo components
	 */
	public InterpreterPR(List<String> dirs, List<String> params) {
		super(SemanticsType.PR, new ListenerPR(), dirs, params);	
	}	
}
