package nl.cwi.reo.interpret.terms;

import java.util.ArrayList;
import java.util.List;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of a list of terms.
 */
public final class ListExpression implements TermExpression {
	
	/**
	 * List of term expressions.
	 */
	private final List<TermExpression> list;

	/**
	 * Constructs a new list of terms.
	 * @param list		list of terms
	 */
	public ListExpression(List<TermExpression> list) {
		this.list = list;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Term> evaluate(Scope s, Monitor m) {
		List<Term> terms = new ArrayList<Term>();
		for (TermExpression t : list)
			terms.addAll(t.evaluate(s, m));
		return terms;
	}

}
