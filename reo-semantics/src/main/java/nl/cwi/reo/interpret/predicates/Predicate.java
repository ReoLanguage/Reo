package nl.cwi.reo.interpret.predicates;

import java.util.ArrayList;
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
	 * Constructs an empty list of scopes.
	 */
	public Predicate() {
		this.scopes = new ArrayList<Scope>();
	}

	/**
	 * Constructs a new list of scopes.
	 * @param scopes	list of scopes
	 */
	public Predicate(List<Scope> scopes) {
		this.scopes = scopes;
	}
	
	/**
	 * Gets the list of scopes.
	 * @return list of scopes
	 */
	public List<Scope> getScopes() {
		return scopes;
	}

}
