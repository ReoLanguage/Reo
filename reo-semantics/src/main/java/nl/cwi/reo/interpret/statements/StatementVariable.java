package nl.cwi.reo.interpret.statements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.terms.Term;
import nl.cwi.reo.interpret.terms.VariableTermExpression;
import nl.cwi.reo.interpret.values.BooleanValue;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of a predicate variable.
 */
public class StatementVariable implements PredicateExpression {

	/**
	 * Variable.
	 */
	private VariableTermExpression variable;

	/**
	 * Constructs a new predicate variable.
	 * 
	 * @param variable
	 *            variable
	 */
	public StatementVariable(VariableTermExpression variable) {
		this.variable = variable;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Nullable
	public List<Scope> evaluate(Scope s, Monitor m) {
		List<Term> t = variable.evaluate(s, m);
		if (t == null || t.isEmpty() || !(t.get(0) instanceof BooleanValue)) {
			m.add("Variable " + variable + " must be of type boolean.");
			return null;
		}
		return ((BooleanValue) t.get(0)).getValue() ? Arrays.asList(s) : new ArrayList<Scope>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Identifier> getVariables() {
		Set<Identifier> vars = new HashSet<Identifier>();
		vars.addAll(variable.getVariables());
		return vars;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return variable.toString();
	}

	@Override
	public Set<Identifier> getDefinedVariables(Set<Identifier> defns) {
		// TODO Take care of this!
		return defns;
	}
	
}
