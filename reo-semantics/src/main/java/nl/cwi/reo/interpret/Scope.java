package nl.cwi.reo.interpret;

import java.util.HashMap;

import nl.cwi.reo.interpret.values.Value;
import nl.cwi.reo.interpret.variables.Identifier;

/**
 * A finite set of assignments.
 */
public final class Scope extends HashMap<Identifier, Value> {

	/**
	 * Serial version ID
	 */
	private static final long serialVersionUID = 3999038009879620308L;

	/**
	 * Constructs an empty scope.
	 */
	public Scope() {
	}

}
