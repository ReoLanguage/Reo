package nl.cwi.reo.semantics.predicates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.ports.PortType;
import nl.cwi.reo.semantics.Semantics;
import nl.cwi.reo.semantics.SemanticsType;
import nl.cwi.reo.util.Monitor;

public class Predicate implements Semantics<Predicate> {

	private final Formula f;

	/**
	 * Constructs a true predicate, which is the identity with respect to
	 * composition.
	 */
	public Predicate() {
		this.f = new Relation("constant", true, new ArrayList<Term>());
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
	public @Nullable Predicate evaluate(Scope s, Monitor m) {
		return new Predicate(f.evaluate(s, m));
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
			Formula transition = null;
			for (Port x : inps) {
				if (!x.equals(p)) {
					Term t_null = new Function("constant", null, new ArrayList<Term>());
					Formula neq = new Negation(new Equality(new Node(x), t_null));
					if (transition == null)
						transition = neq;
					else
						transition = new Conjunction((Arrays.asList(transition, neq)));
				}
			}
			for (Port x : outs) {
				Formula eq = new Equality(new Node(p), new Node(x));
				if (transition == null)
					transition = eq;
				else
					transition = new Conjunction(Arrays.asList(transition, eq));
			}
			transitions.add(transition);
		}

		return new Predicate(new Conjunction(transitions));
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
		return new Predicate(new Conjunction(list));
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
				g = new Existential(new Node(p), g);
		return new Predicate(g);
	}

}
