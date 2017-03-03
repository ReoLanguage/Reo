package nl.cwi.reo.interpret.signatures;

import java.util.Map;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;

/**
 * Result after evaluating a signature expression in a given set of parameter
 * values and ports.
 */
public final class Signature {

	/**
	 * Interface renaming.
	 */
	private final Map<Port, Port> renaming;

	/**
	 * Parameter assignments.
	 */
	private final Scope scope;

	/**
	 * Constructs a new signature.
	 * 
	 * @param renaming
	 *            interface renaming
	 * @param scope
	 *            parameter assignment
	 */
	public Signature(Map<Port, Port> renaming, Scope scope) {
		this.renaming = renaming;
		this.scope = scope;
	}

	/**
	 * Gets the interface consisting of a map that assigns a new port to a
	 * subset of existing ports, and hides all other ports outside of this
	 * subset.
	 * 
	 * @return renaming map
	 */
	public Map<Port, Port> getInterface() {
		return renaming;
	}

	/**
	 * Gets the assignment of all parameter in this signature.
	 * 
	 * @return parameter assignment.
	 */
	public Scope getAssignments() {
		return scope;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "" + renaming + scope;
	}
}
