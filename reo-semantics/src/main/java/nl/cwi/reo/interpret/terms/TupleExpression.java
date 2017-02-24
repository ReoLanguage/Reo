package nl.cwi.reo.interpret.terms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of a list of terms (tuple).
 */
public final class TupleExpression implements TermExpression {
	
	/**
	 * List of term expressions.
	 */
	private final List<TermExpression> list;

	/**
	 * Constructs a new list of terms.
	 * @param list		list of terms
	 */
	public TupleExpression(List<TermExpression> list) {
		this.list = list;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Term> evaluate(Scope s, Monitor m) {
		List<List<Term>> terms = new ArrayList<List<Term>>();
		for (TermExpression t : list)
			terms.add(t.evaluate(s, m));
		return Arrays.asList(new Tuple(terms));
	}

}
