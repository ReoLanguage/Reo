package nl.cwi.reo.interpret.terms;

import java.util.ArrayList;
import java.util.List;

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
	 * @param variable		variable
	 */
	public VariableTermExpression(VariableExpression variable) {
		this.variable = variable;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Term> evaluate(Scope s, Monitor m) {
		List<Term> terms = new ArrayList<Term>();
		Term t = null;
		for (Identifier x : this.variable.evaluate(s, m))
			terms.add((t = s.get(x)) != null ? t : x);
		return terms;
	}
}
