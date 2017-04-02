package nl.cwi.reo.semantics.predicates;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.util.Monitor;

public class Relation implements Formula {
	
	private final String name;
	
	private Object value;
	
	private final List<Term> args;

	public Relation(String name, List<Term> args) {
		this.name = name;
		this.args = args;
	}
	
	public Relation(String name, Object value, List<Term> args) {
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
	public Set<Variable> getFreeVariables() {
		Set<Variable> vars = new HashSet<Variable>();
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
	public Map<Port, Term> getAssignment() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @Nullable Formula evaluate(Scope s, Monitor m) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Port> getInterface() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Formula rename(Map<Port, Port> links) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Formula NNF() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Formula DNF() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Formula QE() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Formula Substitute(Term t, Variable x) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Variable, Integer> getEvaluation() {
		// TODO Auto-generated method stub
		return null;
	}

}
