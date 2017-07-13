package nl.cwi.reo.semantics.predicates;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.util.Monitor;

public class Universal implements Formula {

	private final Variable x;
	
	private final Formula f;

	public Universal(Variable x, Formula f) {
		this.x = x;
		this.f = f;
	}
	
	public Variable getVariable() {
		return x;
	}
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
		return null;
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
	public Formula QE() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Formula substitute(Term t, Variable x) {
		if (this.x.equals(x))
			return this;
		return new Universal(x, f.substitute(t, x));
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
		return  "\u2200" + x + "." + f;  
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
		Universal u = (Universal)other;
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
