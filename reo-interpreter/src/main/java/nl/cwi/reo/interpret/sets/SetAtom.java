package nl.cwi.reo.interpret.sets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.stringtemplate.v4.ST;

import nl.cwi.reo.interpret.Atom;
import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.connectors.ReoConnectorAtom;
import nl.cwi.reo.interpret.instances.Instance;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of an atomic set definition.
 */
public final class SetAtom implements SetExpression {

	/**
	 * Component name.
	 */
	@Nullable
	private final String name;

	/**
	 * Reo semantics object.
	 */
	private final List<Atom> atoms;

	/**
	 * Constructs a new atomic set.
	 * 
	 * @param name
	 *            component name
	 * @param atoms
	 *            semantics objects
	 */
	public SetAtom(@Nullable String name, List<Atom> atoms) {
		this.name = name;
		this.atoms = atoms;
	}

	/**
	 * {@inheritDoc}
	 */
	@Nullable
	public String getName() {
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Nullable
	public Instance evaluate(Scope s, Monitor m) {
		List<Atom> _atoms = new ArrayList<>();
		Map<Port, Port> links = new HashMap<>();
		for (Atom x : this.atoms) {
			Atom y = x.evaluate(s, m);
			_atoms.add(y);
			for (Port p : y.getInterface())
				links.put(p, p);
		}
		return new Instance(new ReoConnectorAtom(name, _atoms, links), new HashSet<>());
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
		ST st = new ST("{\n  <atoms;separator=\"\n\">\n}");
		st.add("atoms", atoms);
		return st.render();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canEvaluate(Set<Identifier> deps) {
		return true;
	}
}
