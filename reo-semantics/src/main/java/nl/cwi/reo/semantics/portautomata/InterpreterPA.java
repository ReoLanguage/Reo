package nl.cwi.reo.semantics.portautomata;

import java.util.List;

import nl.cwi.reo.interpret.SemanticsType;
import nl.cwi.reo.interpret.interpreters.Interpreter;
import nl.cwi.reo.util.Monitor;

// TODO: Auto-generated Javadoc
/**
 * The Class InterpreterPA.
 */
public class InterpreterPA extends Interpreter<PortAutomaton> {

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
	public InterpreterPA(List<String> dirs, List<String> values, Monitor monitor) {
		super(SemanticsType.PA, new ListenerPA(monitor), dirs, values, monitor);
	}
}
