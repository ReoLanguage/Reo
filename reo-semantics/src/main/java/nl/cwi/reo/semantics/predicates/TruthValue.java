package nl.cwi.reo.semantics.predicates;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.typetags.TypeTag;
import nl.cwi.reo.util.Monitor;

/**
 * An atomic nullary predicate (i.e., a boolean value).
 */
public final class TruthValue implements Formula {

	/** Boolean value. */
	private final boolean b;

	/**
	 * Constructs a boolean value from a boolean.
	 * 
	 * @param b
	 *            boolean
	 */
	public TruthValue(boolean b) {
		this.b = b;
	}

	/**
	 * Gets the value of this boolean.
	 *
	 * @return the boolean value of this boolean value.
	 */
	public boolean getValue() {
		return b;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public @Nullable Formula evaluate(Scope s, Monitor m) {
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
	public Set<Port> getPorts() {
		return new HashSet<>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isQuantifierFree() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Formula rename(Map<Port, Port> links) {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Formula NNF() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Formula DNF() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Formula substitute(Term t, Variable x) {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<Variable, Integer> getEvaluation() {
		return new HashMap<>();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return b ? "\u22A4" : "\u22A5";
	}

	@Override
	public Set<Set<Term>> inferTermType(Set<Set<Term>> termTypeSet) {
		// TODO Auto-generated method stub
		return termTypeSet;
	}

	@Override
	public Formula getTypedFormula(Map<Term, TypeTag> typeMap) {
		// TODO Auto-generated method stub
		return this;
	}

}
