package nl.cwi.reo.semantics.predicates;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.ports.Port;

public class MemoryCell implements Variable {
	
	public static final boolean memory = true;
	
	private final int k;
	
	private final String type;
	
	private final boolean prime;
	
	public MemoryCell(int k, boolean prime) {
		this.k = k;
		this.prime = prime;
		this.type=null;
	}

	public MemoryCell(int k, boolean prime, String type) {
		this.k = k;
		this.prime = prime;
		this.type=type;
	}
	
	public MemoryCell setType(String type){
		return new MemoryCell(k,prime,type);
	}
	
	public String getName() {
		return "q" + k;
	}

	public int getIndice(){
		return k;
	}
	
	public String getType(){
		return type;
	}
	
	public boolean hasPrime() {
		return prime;
	}
	
	@Override
	public boolean hadOutputs() {
		return false;
	}
	
	public String toString(){
		if(prime)
			return getName()+"'";
		return getName();
	}

	@Override
	public Term rename(Map<Port, Port> links) {
		return this;
	}

	@Override
	public Term Substitute(Term t, Variable x) {
		if (this.equals(x))
			return t;
		return this;
	}

	@Override
	public Set<Variable> getFreeVariables() {
		return new HashSet<Variable>(Arrays.asList(this));
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
		if (!(other instanceof MemoryCell))
			return false;
		MemoryCell p = (MemoryCell) other;
		return Objects.equals(this.getName(), p.getName()) && Objects.equals(this.prime, p.prime);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.getName(), this.prime);
	}
}
