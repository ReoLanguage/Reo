package nl.cwi.reo.semantics.predicates;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.stringtemplate.v4.ST;

import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.typetags.TypeTag;

public class Function implements Term {
	
	public static final boolean function = true;
	
	private final String name;
	
	private Object value;
	
	private final List<Term> args;
	
	public Function(String name, List<Term> args) {
		this.name = name;
		this.args = args;
	}
	
	public Function(String name, Object value, List<Term> args) {
		this.name = name;
		this.value = value;
		this.args = args;
	}
	
	public String getName() {
		return name;
	}
	
	public Object getValue() {
		if(value == null)
			return "null";
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
	public boolean hadOutputs() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Term rename(Map<Port, Port> links) {
		List<Term> list = new ArrayList<Term>();
		for (Term s : args)
			list.add(s.rename(links));
		return new Function(name, value, list);
	}

	@Override
	public Term Substitute(Term t, Variable x) {
		List<Term> list = new ArrayList<Term>();
		for (Term s : args)
			list.add(s.Substitute(t, x));
		return new Function(name, value, list);
	}

	@Override
	public Set<Variable> getFreeVariables() {
		Set<Variable> vars = new HashSet<Variable>();
		for (Term t : args) 
			vars.addAll(t.getFreeVariables());
		return vars;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TypeTag getTypeTag() {
		// TODO infer type tags of functions.
		return null;
	}

}
