package nl.cwi.reo.semantics.constraintautomata;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.values.StringValue;
import nl.cwi.reo.interpret.values.Value;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.util.Monitor;

public final class Function implements Term {

	private final String name;
	
	private final String reference;
	
	private final List<Term> terms;
	
	public Function(String name, List<Term> terms) {
		if (name == null)
			throw new NullPointerException();
		this.name = name;
		this.reference = null;
		this.terms = terms;
	}
	
	public Function(String name, String reference, List<Term> terms) {
		if (name == null)
			throw new NullPointerException();
		this.name = name;
		this.reference = reference;
		this.terms = terms;
	}

	@Override
	public Term evaluate(Scope s, Monitor m) {
		Value e = s.get(new Identifier(name));
		if (e instanceof StringValue)
			;
		return null;
	}

	@Override
	public boolean contains(String variable) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Term substitute(String variable, Term expression) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Term rename(Map<Port, Port> links) {
		return null;
	}
	
	@Override
	public boolean equals(Object other) {
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof Function)) return false;
	    Function p = (Function)other;
	    boolean equal = Objects.equals(this.name, p.name);
	    Iterator<Term> this_it = this.terms.iterator();
	    Iterator<Term> othr_it = p.terms.iterator();
	    while (this_it.hasNext() && othr_it.hasNext())
	    	equal = equal && this_it.next().equals(othr_it.next());
	    if (this_it.hasNext() || othr_it.hasNext())
	    	return false;
	   	return equal;
	}
	
	@Override
	public int hashCode() {
	    return Objects.hash(this.name);
	}
	
	@Override
	public String toString() {
		return name + terms;
	}
}
