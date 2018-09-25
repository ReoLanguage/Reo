package nl.cwi.reo.interpret.terms;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nl.cwi.reo.interpret.values.Value;

// TODO: Auto-generated Javadoc
/**
 * A list of terms, which is a term in itself.
 */
public final class Tuple extends ArrayList<List<Term>> implements Value {

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 886556264207970624L;

	/**
	 * Constructs a new tuple.
	 *
	 * @param list
	 *            list of terms
	 */
	public Tuple(List<List<Term>> list) {
		super.addAll(list);
	}

	/**
	 * Checks if all iterators in a list have a next term.
	 * 
	 * @param iters
	 *            list of term iterators.
	 * @return true, if all iterators have a next term, and false otherwise.
	 */
	public static boolean hasNext(List<Iterator<Term>> iters) {
		boolean haveNext = true;
		for (Iterator<Term> it : iters)
			haveNext &= it.hasNext();
		return haveNext;
	}

	/**
	 * Checks if all iterators in a list have a next term.
	 * 
	 * @param iters
	 *            list of term iterators.
	 * @return true, if all iterators have a next term, and false otherwise.
	 */
	public static List<Term> next(List<Iterator<Term>> iters) {
		List<Term> list = new ArrayList<Term>();
		for (Iterator<Term> it : iters)
			list.add(it.next());
		return list;
	}
}
