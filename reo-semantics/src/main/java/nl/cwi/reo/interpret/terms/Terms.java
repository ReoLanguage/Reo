package nl.cwi.reo.interpret.terms;

import java.util.List;

/**
 * A list of identifiers, component definitions, component instances,
 * and data items, such as strings, integers, booleans, etc.
 */
public final class Terms {

	/**
	 * List of terms.
	 */
	private final List<Term> list;
	
	/**
	 * Constructs a new list of terms.
	 * @param list 	list of terms
	 */
	public Terms(List<Term> list) {
		this.list = list;
	}

	/**
	 * Gets the list of terms.
	 * @return term list
	 */
	public List<Term> getList() {
		return list;
	}
}
