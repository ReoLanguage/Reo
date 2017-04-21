package nl.cwi.reo.interpret.terms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.stringtemplate.v4.ST;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.values.StringValue;
import nl.cwi.reo.interpret.variables.Identifier;
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
	 * 
	 * @param list
	 *            list of terms
	 */
	public ListExpression(List<TermExpression> list) {
		this.list = list;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Nullable
	public List<Term> evaluate(Scope s, Monitor m) {
		List<Term> terms = new ArrayList<Term>();
		for (TermExpression t : list) {
			List<Term> lst = t.evaluate(s, m);
			if (lst == null)
				return null;
			terms.addAll(lst);
		}
		return terms;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Identifier> getVariables() {
		Set<Identifier> vars = new HashSet<Identifier>();
		for (TermExpression t : list)
			vars.addAll(t.getVariables());
		return vars;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		ST st = new ST("<{list; separator=\", \"}>", '{', '}');
		st.add("list", list);
		return st.render();
	}
}
