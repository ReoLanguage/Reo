package nl.cwi.reo.semantics.predicates;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.stringtemplate.v4.ST;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.util.Monitor;

public class Relation implements Formula {

	public static final boolean relation = true;

	private final String name;

	private String value;

	@Nullable
	private final List<Term> args;

	public Relation(String name, List<Term> args) {
		this.name = name;
		this.args = args;
	}

	public Relation(String name, String value, List<Term> args) {
		this.name = name;
		this.value = value;
		this.args = args;
	}

	public String getName() {
		return name;
	}

	public Object getValue() {
		return value;
	}

	public List<Term> getArgs() {
		return args;
	}

	@Override
	public String toString() {
		ST st = new ST("<name><if(args)>(<args; separator=\", \">)<endif>");
		st.add("name", name);
		st.add("args", args);
		return st.render();
	}

	@Override
	public Set<Variable> getFreeVariables() {
		Set<Variable> vars = new HashSet<Variable>();
		if (args != null)
			for (Term t : args)
				vars.addAll(t.getFreeVariables());
		return vars;
	}

	@Override
	public Formula getGuard() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Variable, Term> getAssignment() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @Nullable Formula evaluate(Scope s, Monitor m) {
		return this;
	}

	@Override
	public Set<Port> getInterface() {
		return null;
	}

	@Override
	public Formula rename(Map<Port, Port> links) {
		return this;
	}

	@Override
	public Formula NNF() {
		return this;
	}

	@Override
	public Formula DNF() {
		return this;
	}

	@Override
	public Formula QE() {
		return this;
	}

	@Override
	public Formula Substitute(Term t, Variable x) {
		if (args == null)
			return this;
		List<Term> listTerms = new ArrayList<Term>();
		for (Term term : args) {
			if (term.equals(t))
				listTerms.add(x);
			else
				listTerms.add(term);
		}
		return new Relation(this.name, this.value, listTerms);
	}

	@Override
	public Map<Variable, Integer> getEvaluation() {
		return null;
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
		if (!(other instanceof Relation))
			return false;
		Relation p = (Relation) other;
		return Objects.equals(this.name, p.name) && Objects.equals(this.value, p.value)
				&& Objects.equals(this.args, p.args);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.getName(), this.value, this.args);
	}

}
