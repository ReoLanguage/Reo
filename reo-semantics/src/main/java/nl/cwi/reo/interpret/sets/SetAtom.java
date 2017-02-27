package nl.cwi.reo.interpret.sets;

import java.util.HashSet;
import java.util.Set;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.connectors.ReoConnectorAtom;
import nl.cwi.reo.interpret.connectors.SourceCode;
import nl.cwi.reo.interpret.instances.Instance;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.semantics.Semantics;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of an atomic set definition.
 * @param <T> Reo semantics type
 */
public final class SetAtom<T extends Semantics<T>> implements SetExpression<T> {
	
	/**
	 * Reo semantics object.
	 */
	private final T atom;
	
	/**
	 * Reference to source code.
	 */
	private final SourceCode source;

	/**
	 * Constructs a new atomic set.
	 * @param atom		semantics object
	 * @param source	reference to source code
	 */
	public SetAtom(T atom, SourceCode source) {
		this.atom = atom;
		this.source = source;
	}

	/**
	 * Evaluates this atomic set to an instance containing an atomic Reo connector.
	 */
	@Override
	public Instance<T> evaluate(Scope s, Monitor m) {
		return new Instance<T>(new ReoConnectorAtom<T>(atom.evaluate(s, m), source), new HashSet<Set<Identifier>>());
	}

}
