package nl.cwi.reo.semantics.predicates;

import java.util.Map;
import java.util.Set;

import nl.cwi.reo.interpret.Expression;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.typetags.TypeTag;

/**
 * A abstract term over the language of constraints that represents a data item.
 * 
 * <p>
 * Every implementing class is immutable.
 */
public interface Term extends Expression<Term> {

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
	public TypeTag getTypeTag();
	
//	/**
//	 * Infere the type tag of this term from a set of set of terms.
//	 * 
//	 * @return this term with infered typetag.
//	 */
//	public Term infereTypeTag(Set<Set<Term>> s);
}
