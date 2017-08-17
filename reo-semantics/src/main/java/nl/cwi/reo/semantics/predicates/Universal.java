package nl.cwi.reo.semantics.predicates;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.util.Monitor;

/**
 * The Class Universal.
 */
public final class Universal implements Formula {

	/**
	 * Quantified variable.
	 */
	private final Variable x;

	/**
	 * Original formula.
	 */
	private final Formula f;
	
	/**
	 * Free variables of this term.
	 */
	private final Set<Variable> vars;

	/**
	 * Constructs an universal quantification of a variable in a formula.
	 * 
	 * @param x
	 *            variable
	 * @param f
	 *            formula
	 */
	public Universal(Variable x, Formula f) {
		this.x = x;
		this.f = f;
		Set<Variable> vars = new HashSet<>(f.getFreeVariables());
		vars.remove(x);
		this.vars = Collections.unmodifiableSet(vars);
	}

	/**
	 * Gets the variable.
	 *
	 * @return the variable
	 */
	public Variable getVariable() {
		return x;
	}

	/**
	 * Gets the formula.
	 *
	 * @return the formula
	 */
	public Formula getFormula() {
		return f;
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
		return new Universal(x, f.rename(newlinks));
	}

	/**
	 * {@inheritDoc}
	 */

	@Override
	public Set<Port> getPorts() {
		Set<Port> P = f.getPorts();
		if (x instanceof PortVariable)
			P.remove(((PortVariable) x).getPort());
		return P;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isQuantifierFree() {
		return false;
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
		return new Universal(x, g);
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
		return new Universal(x, f.NNF());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Formula substitute(Term t, Variable x) {
		if (!x.equals(this.x))
			return new Universal(this.x, f.substitute(t, x));
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Variable> getFreeVariables() {
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
		return "\u2200" + x + "." + f;
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
		if (!(other instanceof Universal))
			return false;
		Universal u = (Universal) other;
		return Objects.equals(this.x, u.x) && Objects.equals(this.f, u.f);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.x, this.f);
	}

}
