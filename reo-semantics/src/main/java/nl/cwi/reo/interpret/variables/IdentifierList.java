package nl.cwi.reo.interpret.variables;

import java.util.List;

/**
 * A list of identifiers of generic type, such as 
 * variable names, ports, nodes, and parameters.
 * @param <I> type of identifier
 */
public final class IdentifierList<I extends Identifier> {
	
	/**
	 * List of identifiers.
	 */
	private final List<I> identifiers;

	/**
	 * Constructs a new list of identifiers.
	 * @param identifiers	list of identifiers
	 */
	public IdentifierList(List<I> identifiers) {
		this.identifiers = identifiers;
	}

	/**
	 * Gets the list of identifiers.
	 * @return list of identifiers.
	 */
	public List<I> getIdentifiers() {
		return identifiers;
	}
}
