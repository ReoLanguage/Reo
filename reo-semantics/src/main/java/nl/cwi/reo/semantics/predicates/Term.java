package nl.cwi.reo.semantics.predicates;

import java.util.Map;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.typetags.TypeTag;

/**
 * A abstract term over the language of constraints that represents a data item.
 */
public interface Term {

	/**
	 * Checks if this term contains an output port variable.
	 * 
	 * @return true if this term contains an outpout port variable.
	 */
	public boolean hadOutputs();

	/**
	 * Renames the port variables in this term.
	 * 
	 * @param links
	 *            map that assigns a new port to an old port.
	 * @return term wherein old ports are substituted by new ports.
	 */
	public Term rename(Map<Port, Port> links);

	/**
	 * Substitutes a term t for every occurrence of a variable x in this term.
	 * 
	 * @param t
	 *            substituted term
	 * @param x
	 *            free variable
	 * @return substituted term.
	 */
	public Term substitute(Term t, Variable x);

	/**
	 * Gets the set of all free variables in this term.
	 * 
	 * @return set of all free variables.
	 */
	public Set<Variable> getFreeVariables();

	/**
	 * Gets the type tag of this term.
	 * 
	 * @return type tag of this term.
	 */
	@Nullable
	public TypeTag getTypeTag();
}
