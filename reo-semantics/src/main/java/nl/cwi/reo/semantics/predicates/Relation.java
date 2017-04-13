package nl.cwi.reo.semantics.predicates;

import java.util.ArrayList;
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
		List<Term> listTerms = new ArrayList<Term>();
		for(Term term : args){
			if(term.equals(t))
				listTerms.add(x);
			else
				listTerms.add(term);
		}
		return new Relation(this.name,this.value,listTerms);
	}

	@Override
	public Map<Variable, Integer> getEvaluation() {
		return null;
	}

}
