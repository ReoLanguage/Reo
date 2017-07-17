// package nl.cwi.reo.interpret.interpreters;
//
// import java.util.List;
//
// import nl.cwi.reo.interpret.listeners.ListenerCAM;
// import nl.cwi.reo.semantics.SemanticsType;
// import nl.cwi.reo.semantics.constraintautomata.ConstraintAutomaton;
//
// import nl.cwi.reo.util.Monitor;
//
// public class InterpreterCAM extends Interpreter<ConstraintAutomaton> {
//
// /**
// * Constructs a Reo interpreter for Constraint Automaton with Memory
// semantics.
// * @param dirs list of directories of Reo components
// * @param values parameter values of main component
// * @param monitor message container
// */
// public InterpreterCAM(List<String> dirs, List<String> values, Monitor
// monitor) {
// super(SemanticsType.CAM, new ListenerCAM(monitor), dirs, values, monitor);
// }
// }
