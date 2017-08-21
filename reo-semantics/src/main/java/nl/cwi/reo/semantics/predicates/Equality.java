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
import nl.cwi.reo.util.Monitor;

/**
 * An equality of two terms.
 */
public final class Equality implements Formula {

	/**
	 * Flag for string template.
	 */
	public static final boolean equality = true;

	/**
	 * Left-hand-side of the equality.
	 */
	private final Term t1;

	/**
	 * Right-hand-side of the equality.
	 */
	private final Term t2;
	
	/**
	 * Free variables of this term.
	 */
	private final Set<Variable> vars;

	/**
	 * Constructs an equality of two terms.
	 * 
	 * @param t1
	 *            left-hand-side term.
	 * @param t2
	 *            right-hand-side term.
	 */
	public Equality(Term t1, Term t2) {
		this.t1 = t1;
		this.t2 = t2;
		Set<Variable> vars = new HashSet<>(t1.getFreeVariables());
		vars.addAll(t2.getFreeVariables());
		this.vars = Collections.unmodifiableSet(vars);
	}

	/**
	 * Gets the left-hand-side of this equality.
	 * 
	 * @return term on the left-hand-side of this equality.
	 */
	public Term getLHS() {
		return t1;
	}

	/**
	 * Gets the right-hand-side of this equality.
	 * 
	 * @return term on the right-hand-side of this equality.
	 */
	public Term getRHS() {
		return t2;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Formula rename(Map<Port, Port> links) {
		return new Equality(t1.rename(links), t2.rename(links));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Port> getPorts() {
		// TODO Auto-generated method stub
		Set<Port> portsInFormula = new HashSet<Port>();
		if (t1 instanceof PortVariable) {
			portsInFormula.add(((PortVariable) t1).getPort());
		}
		if (t2 instanceof PortVariable) {
			portsInFormula.add(((PortVariable) t2).getPort());
		}
		return portsInFormula;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isQuantifierFree() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public @Nullable Formula evaluate(Scope s, Monitor m) {
		Term u1 = t1.evaluate(s, m);
		Term u2 = t2.evaluate(s, m);
		if (u1 == null || u2 == null)
			return null;
		return new Equality(u1, u2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Formula DNF() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Formula NNF() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Formula substitute(Term t, Variable x) {
		if (!vars.contains(x))
			return this;
		return new Equality(t1.substitute(t, x), t2.substitute(t, x));
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
		Map<Variable, Integer> map = new HashMap<Variable, Integer>();
		if (t1 instanceof NullValue && t2 instanceof Variable) {
			map.put((Variable) t2, 0);
		} else if (t2 instanceof NullValue && t1 instanceof Variable) {
			map.put((Variable) t1, 0);
		}
		return map;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return t1 + " = " + t2;
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
		if (!(other instanceof Equality))
			return false;
		Equality eq = (Equality) other;
		return (Objects.equals(this.t1, eq.t1) && Objects.equals(this.t2, eq.t2))
				|| Objects.equals(this.t1, eq.t2) && Objects.equals(this.t2, eq.t1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.t1, this.t2);
	}
}
