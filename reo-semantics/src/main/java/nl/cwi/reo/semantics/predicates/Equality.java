package nl.cwi.reo.semantics.predicates;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.util.Monitor;

public class Equality implements Formula {
	
	/**
	 * Flag for string template.
	 */
	public static final boolean equality = true;

	private final Term t1;

	private final Term t2;

	public Equality(Term t1, Term t2) {
		this.t1 = t1;
		this.t2 = t2;
	}
	
	public Term getLHS() {
		return t1;
	}

	public Term getRHS() {
		return t2;
	}
	
	@Override
	public Formula rename(Map<Port, Port> links) {
		Term s1 = t1;
		if (t1 instanceof Node) {
			Port b = links.get(((Node) t1).getPort());
			if (b != null)
				s1 = new Node(b);
		}
		if(t1 instanceof Function) {
			s1 = t1.rename(links);
		}

		Term s2 = t2;
		if (t2 instanceof Node) {
			Port b = links.get(((Node) t2).getPort());
			if (b != null)
				s2 = new Node(b);
		}
		return new Equality(s1, s2);
	}

	@Override
	public Set<Port> getInterface() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String toString(){
		return t1 + "=" + t2;
	}

	@Override
	public @Nullable Formula evaluate(Scope s, Monitor m) {
		Term t_1 = t1;
		Term t_2 = t2;
		if(t1 instanceof Function)
			t_1 = ((Function) t1).evaluate(s, m);
		if(t2 instanceof Function)
			t_2 = ((Function) t2).evaluate(s, m);
		return new Equality(t_1,t_2);
	}

	@Override
	public Formula DNF() {
		return this;
	}

	@Override
	public Formula NNF() {
		return this;
	}

	@Override
	public Formula QE() {
		return this;
	}

	@Override
	public Formula Substitute(Term t, Variable x) {
		Term t_1 = t1.Substitute(t, x);
		Term t_2 = t2.Substitute(t, x);
		if(t_1.equals(t_2))
			return new Relation("true","true",null);
		return new Equality(t_1, t_2);
	}

	@Override
	public Set<Variable> getFreeVariables() {
		Set<Variable> vars = t1.getFreeVariables();
		vars.addAll(t2.getFreeVariables());
		return vars;
	}

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
	public boolean equals(@Nullable Object other) {
		if (other == null)
			return false;
		if (other == this)
			return true;
		if (!(other instanceof Equality))
			return false;
		Equality eq = (Equality) other;
		return (Objects.equals(this.t1, eq.t1)&&Objects.equals(this.t2, eq.t2))||
				Objects.equals(this.t1, eq.t2)&&Objects.equals(this.t2, eq.t1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.t1,this.t2);
	}
}
