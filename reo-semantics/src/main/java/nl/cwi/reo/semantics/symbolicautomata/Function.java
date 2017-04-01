package nl.cwi.reo.semantics.symbolicautomata;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.cwi.reo.interpret.ports.Port;

public class Function<T> implements Term {
	
	private final T id;
	
	private final List<Term> args;
	
	public Function(T id, List<Term> args) {
		this.id = id;
		this.args = args;
	}
	
	public T getId() {
		return id;
	}

	public List<Term> getArgs() {
		return args;
	}
	
	@Override
	public boolean hadOutputs() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Term rename(Map<Port, Port> links) {
		List<Term> list = new ArrayList<Term>();
		for (Term s : args)
			list.add(s.rename(links));
		return new Function<T>(id, list);
	}

	@Override
	public Term Substitute(Term t, Variable x) {
		List<Term> list = new ArrayList<Term>();
		for (Term s : args)
			list.add(s.Substitute(t, x));
		return new Function<T>(id, list);
	}

	@Override
	public Set<Variable> getFreeVariables() {
		Set<Variable> vars = new HashSet<Variable>();
		for (Term t : args) 
			vars.addAll(t.getFreeVariables());
		return vars;
	}

}
