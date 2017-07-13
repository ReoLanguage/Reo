package nl.cwi.reo.interpret.interpreters;

import java.util.List;

import nl.cwi.reo.interpret.listeners.ListenerP;
import nl.cwi.reo.semantics.SemanticsType;
import nl.cwi.reo.semantics.predicates.Predicate;
import nl.cwi.reo.util.Monitor;

public class InterpreterP extends Interpreter<Predicate> {
	
	/**
	 * Constructs a Reo interpreter for symbolic automaton semantics.
	 * @param dirs		list of directories of Reo components
	 * @param values	parameter values of main component
	 * @param monitor	message container
	 */
	public InterpreterP(List<String> dirs, List<String> values, Monitor monitor) {
		super(SemanticsType.P, new ListenerP(monitor), dirs, values, monitor);	
	}	
}
