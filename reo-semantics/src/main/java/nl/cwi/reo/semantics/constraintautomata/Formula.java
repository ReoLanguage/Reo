package nl.cwi.reo.semantics.constraintautomata;

import java.util.Collection;
import java.util.Map;

import nl.cwi.reo.interpret.Expression;
import nl.cwi.reo.interpret.ports.Port;

public interface Formula extends Expression<Formula> {
	
	/**
	 * Gets a variable outside of a given interface, or null
	 * if such a variable does not exist.
	 * @return variable name, or null.
	 */
	public String getInternalName(Collection<? extends Port> intface);
	
	/**
	 * Finds, if possible, an expression of a given variable in terms 
	 * of other (different) variables.
	 * @param variable		name of variable
	 * @return Term equivalent to the given variable, or null if this 
	 * term cannot be found. 
	 */
	public Term findAssignment(String variable);
	
	/**
	 * Substitutes a given variable name with a given expression, 
	 * and simplifies the resulting formula.
	 * @param variable		variable name
	 * @param expression	expression equivalent to this variable
	 * @return Substituted formula.
	 */
	public Formula substitute(String variable, Term expression);
	
	/**
	 * Renames all variables in this data constraint, according 
	 * to a renaming map
	 * @param links		renaming map
	 * @return Formula with renamed variables.
	 */
	public Formula rename(Map<Port, Port> links);
	
}
