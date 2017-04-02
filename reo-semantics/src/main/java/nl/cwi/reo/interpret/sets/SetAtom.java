package nl.cwi.reo.interpret.sets;

import java.util.HashSet;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.stringtemplate.v4.ST;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.connectors.ReoConnectorAtom;
import nl.cwi.reo.interpret.connectors.Reference;
import nl.cwi.reo.interpret.instances.Instance;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.semantics.Semantics;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of an atomic set definition.
 * 
 * @param <T>
 *            Reo semantics type
 */
public final class SetAtom<T extends Semantics<T>> implements SetExpression<T> {
	
	/**
	 * Component name.
	 */
	private final String name;

	/**
	 * Reo semantics object.
	 */
	private final T atom;

	/**
	 * Reference to source code.
	 */
	private final Reference source;

	/**
	 * Constructs a new atomic set.
	 * 
	 * @param name
	 *            component name
	 * @param atom
	 *            semantics object
	 * @param source
	 *            reference to source code
	 */
	public SetAtom(String name, T atom, Reference source) {
		if (atom == null || source == null)
			throw new NullPointerException();
		this.name = name;
		this.atom = atom;
		this.source = source;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Nullable
	public String getName() {
		return name;
	}

	/**
	 * Evaluates this atomic set to an instance containing an atomic Reo
	 * connector.
	 */
	@Override
	@Nullable
	public Instance<T> evaluate(Scope s, Monitor m) {
		T semantics = atom.evaluate(s, m);
		if (semantics == null)
			return null;
		return new Instance<T>(new ReoConnectorAtom<T>(name, semantics, source), new HashSet<Set<Identifier>>());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Identifier> getVariables() {
		return new HashSet<Identifier>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		ST st = new ST("{\n  <atom>\n|\n  <source>\n}");
		st.add("atom", atom);
		st.add("source", source);
		return st.render();
	}
}
