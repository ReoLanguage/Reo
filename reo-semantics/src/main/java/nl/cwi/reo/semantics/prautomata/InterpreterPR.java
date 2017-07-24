package nl.cwi.reo.semantics.prautomata;

import java.util.List;

import nl.cwi.reo.interpret.SemanticsType;
import nl.cwi.reo.interpret.interpreters.Interpreter;
import nl.cwi.reo.util.Monitor;

// TODO: Auto-generated Javadoc
/**
 * The Class InterpreterPR.
 */
public class InterpreterPR extends Interpreter<PRAutomaton> {

	/**
	 * Constructs a Reo interpreter for Port Automaton semantics.
	 * 
	 * @param dirs
	 *            list of directories of Reo components
	 * @param values
	 *            parameter values of main component
	 * @param monitor
	 *            message container
	 */
	public InterpreterPR(List<String> dirs, List<String> values, Monitor monitor) {
		super(SemanticsType.PR, new ListenerPR(monitor), dirs, values, monitor);
	}
}
