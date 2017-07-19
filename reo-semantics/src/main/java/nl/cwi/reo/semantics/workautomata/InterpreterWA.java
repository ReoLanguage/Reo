package nl.cwi.reo.semantics.workautomata;

import java.util.List;

import nl.cwi.reo.interpret.SemanticsType;
import nl.cwi.reo.interpret.interpreters.Interpreter;
import nl.cwi.reo.util.Monitor;

// TODO: Auto-generated Javadoc
/**
 * The Class InterpreterWA.
 */
public class InterpreterWA extends Interpreter<WorkAutomaton> {

	/**
	 * Constructs a Reo interpreter for Work Automaton semantics.
	 * 
	 * @param dirs
	 *            list of directories of Reo components
	 * @param values
	 *            parameter values of main component
	 * @param monitor
	 *            message container
	 */
	public InterpreterWA(List<String> dirs, List<String> values, Monitor monitor) {
		super(SemanticsType.WA, new ListenerWA(monitor), dirs, values, monitor);
	}
}
