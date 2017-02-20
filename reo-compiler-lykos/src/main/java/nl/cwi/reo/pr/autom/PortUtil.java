package nl.cwi.reo.pr.autom;

import nl.cwi.reo.pr.autom.AutomatonFactory.Automaton;
import nl.cwi.reo.pr.autom.AutomatonFactory.AutomatonSet;
import nl.cwi.reo.pr.misc.PortFactory.Port;
import nl.cwi.reo.pr.misc.PortFactory.PortSet;

public class PortUtil {

	//
	// FIELDS
	//

	private static final String ANNOTATION_AUTOMATA = "AUTOMATA";

	//
	// METHODS
	//

	static void addAutomaton(Port port, Automaton automaton) {
		if (port == null)
			throw new NullPointerException();
		if (automaton == null)
			throw new NullPointerException();
		if (port.getFactory() != automaton.getPortFactory())
			throw new IllegalArgumentException();

		if (!port.hasAnnotation(ANNOTATION_AUTOMATA, AutomatonSet.class))
			port.addAnnotation(ANNOTATION_AUTOMATA, automaton.getFactory()
					.newSet());

		port.getAnnotation(ANNOTATION_AUTOMATA, AutomatonSet.class).add(
				automaton);
	}

	static void addAutomaton(PortSet ports, Automaton automaton) {
		if (ports == null)
			throw new NullPointerException();
		if (automaton == null)
			throw new NullPointerException();
		if (ports.getFactory() != automaton.getPortFactory())
			throw new IllegalArgumentException();

		for (Port p : ports)
			addAutomaton(p, automaton);
	}

	static AutomatonSet getAutomata(Port port) {
		if (port == null)
			throw new NullPointerException();
		if (!port.hasAnnotation(ANNOTATION_AUTOMATA, AutomatonSet.class))
			throw new IllegalArgumentException();

		return port.getAnnotation(ANNOTATION_AUTOMATA, AutomatonSet.class);
	}

	static boolean isPrivate(Port port) {
		if (port == null)
			throw new NullPointerException();
		if (!port.hasAnnotation(ANNOTATION_AUTOMATA, AutomatonSet.class))
			throw new IllegalArgumentException();
		
		return port.getAnnotation(ANNOTATION_AUTOMATA, AutomatonSet.class)
				.count() != 1;
	}

	static boolean isPublic(Port port) {
		if (port == null)
			throw new NullPointerException();
		if (!port.hasAnnotation(ANNOTATION_AUTOMATA, AutomatonSet.class))
			throw new IllegalArgumentException();

		return port.getAnnotation(ANNOTATION_AUTOMATA, AutomatonSet.class)
				.count() == 1;
	}

	static void removeAutomaton(Port port, Automaton automaton) {
		if (port == null)
			throw new NullPointerException();
		if (automaton == null)
			throw new NullPointerException();
		if (port.getFactory() != automaton.getPortFactory())
			throw new IllegalArgumentException();

		if (port.hasAnnotation(ANNOTATION_AUTOMATA, AutomatonSet.class))
			port.getAnnotation(ANNOTATION_AUTOMATA, AutomatonSet.class).remove(
					automaton);
	}

	static void removeAutomaton(PortSet ports, Automaton automaton) {
		if (ports == null)
			throw new NullPointerException();
		if (automaton == null)
			throw new NullPointerException();
		if (ports.getFactory() != automaton.getPortFactory())
			throw new IllegalArgumentException();

		for (Port p : ports)
			removeAutomaton(p, automaton);
	}
}
