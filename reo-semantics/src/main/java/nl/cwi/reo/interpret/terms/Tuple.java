package nl.cwi.reo.interpret.terms;

import java.util.List;

/**
 * A list of terms, which is a term in itself.
 */
public final class Tuple implements Term {
	
	/**
	 * List of terms.
	 */
	private final List<Term> list;

	/**
	 * Constructs a new tuple
	 * @param list	list of terms
	 */
	public Tuple(List<Term> list) {
		this.list = list;
	}
	
	/**
	 * Gets the list of terms.
	 * @return list of terms
	 */
	public List<Term> getList() {
		return list;
	}

}
