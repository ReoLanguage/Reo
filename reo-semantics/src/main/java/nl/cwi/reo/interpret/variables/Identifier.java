package nl.cwi.reo.interpret.variables;

import java.util.Objects;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.terms.Term;

/**
 * A concatenation of a fully qualified name and 
 * a sequence of indices.
 */
public class Identifier implements Term {

	/**
	 * Name.
	 */
	protected final String name;
	
	/**
	 * Constructs a new identifier.
	 * @param name		identifier name
	 */
	public Identifier(String name) {
		this.name = name;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return name;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(@Nullable Object other) {
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof Identifier)) return false;
	    Identifier p = (Identifier)other;
	   	return Objects.equals(this.name, p.name);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
	    return Objects.hash(this.name);
	}	
}
