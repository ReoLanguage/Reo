package nl.cwi.reo.semantics.predicates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.ports.PortType;
import nl.cwi.reo.semantics.Semantics;
import nl.cwi.reo.semantics.SemanticsType;
import nl.cwi.reo.util.Monitor;

// TODO: Auto-generated Javadoc
/**
 * Predicate semantics of Reo connectors.
 */
public class Predicate implements Semantics<Predicate> {

	/** Formula of this predicate. */
	private final Formula f;

	/**
	 * Constructs a true predicate, which is the identity with respect to
	 * composition.
	 */
	public Predicate() {
		this.f = new TruthValue(true);
	}

	/**
	 * Constructs a new predicate from a given formula.
	 * 
	 * @param f
	 *            formula
	 */
	public Predicate(Formula f) {
		this.f = f;
	}

	/**
	 * Gets the formula of this predicate.
	 * 
	 * @return formula
	 */
	public Formula getFormula() {
		return f;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Nullable
	public Predicate evaluate(Scope s, Monitor m) {
		Formula g = f.evaluate(s, m);
		if (g != null)
			return new Predicate(g);
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Port> getInterface() {
		return f.getInterface();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SemanticsType getType() {
		return SemanticsType.P;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Predicate getNode(Set<Port> node) {
		Set<Port> ports = new HashSet<Port>(node);

		Set<Port> inps = new HashSet<Port>();
		Set<Port> outs = new HashSet<Port>();

		for (Port p : ports) {
			if (p.getType() == PortType.IN) {
				inps.add(p);
			} else {
				outs.add(p);
			}
		}

		List<Formula> transitions = new ArrayList<Formula>();

		for (Port p : inps) {
			Formula transition = new TruthValue(true);
			for (Port x : inps) {
				if (!x.equals(p)) {
					Term t_null = new NullValue();
					Formula neq = new Negation(new Equality(new PortVariable(x), t_null));
					transition = Conjunction.conjunction(Arrays.asList(transition, neq));
				}
			}
			for (Port x : outs) {
				Formula eq = new Equality(new PortVariable(p), new PortVariable(x));
				transition = Conjunction.conjunction(Arrays.asList(transition, eq));
			}
			transitions.add(transition);
		}

		return new Predicate(Conjunction.conjunction(transitions));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Predicate rename(Map<Port, Port> links) {
		return new Predicate(f.rename(links));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Predicate compose(List<Predicate> components) {
		List<Formula> list = new ArrayList<Formula>();
		list.add(this.f);
		for (Predicate A : components)
			list.add(A.f);
		return new Predicate(Conjunction.conjunction(list));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Predicate restrict(Collection<? extends Port> intface) {
		Set<Port> P = f.getInterface();
		Formula g = f;
		for (Port p : P)
			if (!intface.contains(p))
				g = new Existential(new PortVariable(p), g);
		return new Predicate(g);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(@Nullable Object other) {
		if (other == null)
			return false;
		if (other == this)
			return true;
		if (!(other instanceof Predicate))
			return false;

		Predicate p = (Predicate) other;
		return Objects.equals(f, p.getFormula());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(f);
	}

}
