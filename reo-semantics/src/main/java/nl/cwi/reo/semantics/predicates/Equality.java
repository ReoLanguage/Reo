package nl.cwi.reo.semantics.predicates;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.util.Monitor;

// TODO: Auto-generated Javadoc
/**
 * An equality of two terms.
 */
public class Equality implements Formula {

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
		Term s1 = t1;
		if (t1 instanceof PortVariable) {
			Port b = links.get(((PortVariable) t1).getPort());
			if (b != null)
				s1 = new PortVariable(b);
		}
		if (t1 instanceof Function) {
			s1 = t1.rename(links);
		}

		Term s2 = t2;
		if (t2 instanceof PortVariable) {
			Port b = links.get(((PortVariable) t2).getPort());
			if (b != null)
				s2 = new PortVariable(b);
		}
		return new Equality(s1, s2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Port> getInterface() {
		// TODO Auto-generated method stub
		return new HashSet<>();
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
	public Formula QE() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Formula substitute(Term t, Variable x) {
		Term t_1 = t1.substitute(t, x);
		Term t_2 = t2.substitute(t, x);
		if (t_1.equals(t_2))
			return new TruthValue(true);
		return new Equality(t_1, t_2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Variable> getFreeVariables() {
		Set<Variable> vars = t1.getFreeVariables();
		vars.addAll(t2.getFreeVariables());
		return vars;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<Variable, Integer> getEvaluation() {
		Map<Variable, Integer> map = new HashMap<Variable, Integer>();
		if (t1 instanceof Function && ((Function) t1).getValue() == null && t2 instanceof Variable) {
			map.put((Variable) t2, 0);
		} else if (t2 instanceof Function && ((Function) t2).getValue() == null && t1 instanceof Variable) {
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
