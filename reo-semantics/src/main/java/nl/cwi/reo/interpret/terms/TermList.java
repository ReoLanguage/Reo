package nl.cwi.reo.interpret.terms;

import java.util.List;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of a list of terms.
 */
public final class TermList implements TermsExpression {
	
	/**
	 * List of term expressions.
	 */
	private final List<TermsExpression> list;

	/**
	 * Constructs a new list of terms.
	 * @param list		list of terms
	 */
	public TermList(List<TermsExpression> list) {
		this.list = list;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Terms evaluate(Scope s, Monitor m) {
		// TODO Auto-generated method stub
		return null;
	}

}
