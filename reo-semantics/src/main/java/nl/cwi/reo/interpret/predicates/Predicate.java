package nl.cwi.reo.interpret.predicates;

import java.util.List;

import nl.cwi.reo.interpret.Scope;

/**
 * List of scopes that satisfy a predicate.
 */
public final class Predicate {
	
	/**
	 * List of scopes.
	 */
	private final List<Scope> scopes; 

	/**
	 * Constructs a new list of scopes.
	 * @param scopes	list pf scopes
	 */
	public Predicate(List<Scope> scopes) {
		this.scopes = scopes;
	}

}
