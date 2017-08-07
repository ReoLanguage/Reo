package nl.cwi.reo.semantics.predicates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.util.Monitor;

// TODO: Auto-generated Javadoc
/**
 * An existential quantification of a variable in a formula.
 */
public class Existential implements Formula {

	/**
	 * Quantified variable.
	 */
	private final Variable x;

	/**
	 * Original formula.
	 */
	private final Formula f;

	/**
	 * Constructs an existential quantification of a variable in a formula.
	 * 
	 * @param x
	 *            variable
	 * @param f
	 *            formula
	 */
	public Existential(Variable x, Formula f) {
		this.x = x;
		this.f = f;
	}

	/**
	 * Gets the quantified variable.
	 * 
	 * @return quantified variable.
	 */
	public Variable getVariable() {
		return x;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Formula rename(Map<Port, Port> links) {
		Map<Port, Port> newlinks = new HashMap<Port, Port>(links);
		for (Map.Entry<Port, Port> link : links.entrySet())
			if (!link.getKey().getName().equals(x.getName()))
				newlinks.put(link.getKey(), link.getValue());
		return new Existential(x, f.rename(newlinks));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Port> getInterface() {
		Set<Port> P = f.getInterface();
		if (x instanceof PortVariable)
			P.remove(((PortVariable) x).getPort());
		return P;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public @Nullable Formula evaluate(Scope s, Monitor m) {
		Scope s1 = new Scope(s);
		s1.remove(new Identifier(x.getName()));
		Formula g = f.evaluate(s1, m);
		if (g == null)
			return null;
		return new Existential(x, g);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Disjunction DNF() {
		throw new UnsupportedOperationException("DNF assumes a quantifier free formula.");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Formula NNF() {
		return new Existential(x, f.NNF());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Nullable
	public Formula QE() {
		if (!f.getFreeVariables().contains(x))
			return f.QE();
		if (f instanceof Existential) {
			Formula g = f.QE();
			if (g == null)
				return null;
			return new Existential(x, g).QE();
		} else if (f instanceof Equality) {
			Equality e = (Equality) f;
			if (e.getLHS().equals(x) || e.getRHS().equals(x)) {
				return new TruthValue(true);
			} else {
				return null;
			}
		} else if (f instanceof Disjunction) {
			List<Formula> list = new ArrayList<Formula>();
			for (Formula fi : ((Disjunction) f).getClauses())
				list.add(new Existential(x, fi));
			return new Disjunction(list).QE();
		} else if (f instanceof Conjunction) {
			List<Formula> list = new ArrayList<Formula>();
			Term t = null;
			for (Formula fi : ((Conjunction) f).getClauses()) {
				if (t == null && fi instanceof Equality) {
					Equality e = (Equality) fi;
					if (e.getLHS().equals(x)) {
						t = e.getRHS();
					} else if (e.getRHS().equals(x)) {
						t = e.getLHS();
					} else {
						list.add(fi);
					}
				} else {
					list.add(fi);
				}
			}
			if (t != null) {
				switch (list.size()) {
				case 0:
					return new TruthValue(true);
				case 1:
					return list.get(0).substitute(t, x);
				default:
					return new Conjunction(list).substitute(t, x);
				}
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Formula substitute(Term t, Variable x) {
		if (this.x.equals(x))
			return this;
		return new Existential(x, f.substitute(t, x));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Variable> getFreeVariables() {
		Set<Variable> vars = f.getFreeVariables();
		vars.remove(x);
		return vars;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<Variable, Integer> getEvaluation() {
		Map<Variable, Integer> map = f.getEvaluation();
		map.remove(x);
		return map;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "\u2203" + x + "." + f;
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
		if (!(other instanceof Existential))
			return false;
		Existential e = (Existential) other;
		return Objects.equals(this.x, e.x) && Objects.equals(this.f, e.f);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.x, this.f);
	}
}
