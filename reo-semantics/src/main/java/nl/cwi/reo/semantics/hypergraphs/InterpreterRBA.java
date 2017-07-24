package nl.cwi.reo.semantics.hypergraphs;

import java.util.List;

import nl.cwi.reo.interpret.SemanticsType;
import nl.cwi.reo.interpret.interpreters.Interpreter;
import nl.cwi.reo.util.Monitor;

// TODO: Auto-generated Javadoc
/**
 * The Class InterpreterRBA.
 */
public class InterpreterRBA extends Interpreter<ConstraintHypergraph> {

	/**
	 * Constructs a Reo interpreter for symbolic automaton semantics.
	 * 
	 * @param dirs
	 *            list of directories of Reo components
	 * @param values
	 *            parameter values of main component
	 * @param monitor
	 *            message container
	 */
	public InterpreterRBA(List<String> dirs, List<String> values, Monitor monitor) {
		super(SemanticsType.CH, new ListenerRBA(monitor), dirs, values, monitor);
	}
}
