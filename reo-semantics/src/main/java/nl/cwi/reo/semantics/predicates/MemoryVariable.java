package nl.cwi.reo.semantics.predicates;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.typetags.TypeTag;
import nl.cwi.reo.interpret.typetags.TypeTags;
import nl.cwi.reo.util.Monitor;

/**
 * A variable that represents the current or next value of a memory cell.
 */
public final class MemoryVariable implements Variable, Comparable<MemoryVariable> {

	/**
	 * Flag for string template.
	 */
	public static final boolean memory = true;

	/**
	 * Flag for synchronization set.
	 */
	public boolean negated = true;

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
	}
	
	public MemoryVariable(String name, boolean prime) {
		this.name = name;
		this.prime = prime;
		this.type = TypeTags.Object;
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
	public MemoryVariable setTypeTag(TypeTag type) {
		if(getTypeTag()!=null && getTypeTag()!=TypeTags.Object){
			if(type!=null && type!=TypeTags.Object && !getTypeTag().equals(type)){
				try {
					throw new Exception("type mismatch");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return this;
		}
		else
			return new MemoryVariable(name, prime, type);
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
	public Term substitute(Term t, Variable x) {
		if (this.equals(x))
			return t;
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Variable> getFreeVariables() {
		Set<Variable> vars = new HashSet<>();
		vars.add(this);
		return vars;
	}

	/**
	 * A memory cell is never updated.
	 */
	@Override
	public @Nullable Term evaluate(Scope s, Monitor m) {
		return this;
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
		return Objects.equals(name, p.name) && Objects.equals(prime, p.prime);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.getName(), this.prime);
	}

	/**
	 * Anti-lexicographically compares a memory cell by name and prime. Memory cells
	 * with prime are larger than memory cells without. This method is
	 * consistent with equals.
	 */
	@Override
	public int compareTo(MemoryVariable m) {
		if (prime != m.prime)
			return prime ? 1 : -1;
		return name.compareTo(m.name);
	}
}
