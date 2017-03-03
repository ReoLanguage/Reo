package nl.cwi.reo.interpret.terms;

import java.util.ArrayList;
import java.util.List;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.interpret.variables.VariableExpression;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of a term variable.
 */
public class VariableTermExpression implements TermExpression {

	/**
	 * Variable.
	 */
	private VariableExpression variable;

	/**
	 * Constructs a new term variable.
	 * 
	 * @param variable
	 *            variable
	 */
	public VariableTermExpression(VariableExpression variable) {
		this.variable = variable;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Nullable
	public List<Term> evaluate(Scope s, Monitor m) {
		List<Term> terms = new ArrayList<Term>();
		Term t = null;
		List<? extends Identifier> list = this.variable.evaluate(s, m);
		if (list == null)
			return null;
		for (Identifier x : list)
			terms.add((t = s.get(x)) != null ? t : x);
		return terms;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return variable.toString();
	}
}
