package nl.cwi.reo.semantics.predicates;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.typetags.TypeTag;

public class MemCell implements Variable {

	private final String name;

	private final TypeTag type;

	private final boolean prime;

	public MemCell(String name, boolean prime) {
		this.name = name;
		this.prime = prime;
		this.type = null;
	}

	public MemCell(String name, boolean prime, TypeTag type) {
		this.name = name;
		this.prime = prime;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public TypeTag getType() {
		return type;
	}

	public boolean hasPrime() {
		return prime;
	}

	@Override
	public boolean hadOutputs() {
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "$" + name + (prime ? "'" : "");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Term rename(Map<Port, Port> links) {
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Term Substitute(Term t, Variable x) {
		if (this.equals(x))
			return t;
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
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
		if (!(other instanceof MemCell))
			return false;
		MemCell p = (MemCell) other;
		return Objects.equals(this.name, p.name) && Objects.equals(this.prime, p.prime);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.getName(), this.prime);
	}
}
