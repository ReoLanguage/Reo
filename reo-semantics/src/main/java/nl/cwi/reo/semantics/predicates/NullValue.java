package nl.cwi.reo.semantics.predicates;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.typetags.TypeTag;
import nl.cwi.reo.interpret.typetags.TypeTags;
import nl.cwi.reo.util.Monitor;

/**
 * Constant that represents absence of data. This value is used to encode
 * synchronization constraints and empty memory cells.
 */
public final class NullValue implements Term {
	
	/**
	 * Flag for string template.
	 */
	public static final boolean isnull = true;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public @Nullable Term evaluate(Scope s, Monitor m) {
		return this;
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
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Variable> getFreeVariables() {
		return new HashSet<>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TypeTag getTypeTag() {
		return TypeTags.Object;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "*";
	}
}
