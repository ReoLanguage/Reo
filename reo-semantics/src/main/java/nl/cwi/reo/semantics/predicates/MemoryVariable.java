package nl.cwi.reo.semantics.predicates;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.typetags.TypeTag;
import nl.cwi.reo.util.Monitor;

// TODO: Auto-generated Javadoc
/**
 * A variable that represents the current or next value of a memory cell.
 */
public class MemoryVariable implements Variable {

	/**
	 * Flag for string template.
	 */
	public static final boolean memory = true;

	/**
	 * Name of this memory cell.
	 */
	private final String name;

	/**
	 * Type of this memory cell.
	 */
	private final TypeTag type;

	/**
	 * Flag that indicates whether this variable represents the current value
	 * (prime is false) or the next value (prime is true) of the memory cell.
	 */
	private final boolean prime;
	
	/**
	 * Free variables in this formula.
	 */
	private final Set<Variable> freeVars;

	/**
	 * Constructs a new memory cell variable.
	 * 
	 * @param name
	 *            name of the memory cell
	 * @param prime
	 *            current (false) of next (true) value of the memory cell
	 */
	public MemoryVariable(String name, boolean prime) {
		this.name = name;
		this.prime = prime;
		this.type = new TypeTag("");
		this.freeVars = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(this)));
	}

	/**
	 * Constructs a new memory cell variable.
	 * 
	 * @param name
	 *            name of the memory cell
	 * @param prime
	 *            current (false) of next (true) value of the memory cell
	 * @param type
	 *            type of data in the memory cell
	 */
	public MemoryVariable(String name, boolean prime, TypeTag type) {
		this.name = name;
		this.prime = prime;
		this.type = type;
		this.freeVars = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(this)));
	}

	/**
	 * Gets the name of the memory cell of this variable.
	 * 
	 * @return name of the memory cell of this variable.
	 */
	public String getName() {
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TypeTag getTypeTag() {
		return type;
	}

	/**
	 * Checks whether this variable represents the current or next value of the
	 * memory cell.
	 * 
	 * @return false if this variable represents the current value of the memory
	 *         cell, and false otherwise.s
	 */
	public boolean hasPrime() {
		return prime;
	}

	/**
	 * Returns a new memory cell variable with given type tag.
	 * 
	 * @param type
	 *            type tag
	 * @return new memory cell variable with given type tag.
	 */
	public MemoryVariable setType(TypeTag type) {
		return new MemoryVariable(name, prime, type);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasOutputPorts() {
		return false;
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
	public Term substitute(Map<Variable, Term> map) {
		Term t = map.get(this);
		if (t != null)
			return t;
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Variable> getFreeVariables() {
		return freeVars;
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
	public boolean equals(@Nullable Object other) {
		if (other == null)
			return false;
		if (other == this)
			return true;
		if (!(other instanceof MemoryVariable))
			return false;
		MemoryVariable p = (MemoryVariable) other;
		return Objects.equals(this.name, p.name) && Objects.equals(this.prime, p.prime);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.getName(), this.prime);
	}

	/**
	 * A memory cell is never updated.
	 */
	@Override
	public @Nullable Term evaluate(Scope s, Monitor m) {
		return this;
	}
}
