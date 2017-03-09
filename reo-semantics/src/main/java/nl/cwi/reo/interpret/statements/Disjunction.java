package nl.cwi.reo.interpret.statements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.stringtemplate.v4.ST;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of a disjunction.
 */
public final class Disjunction implements PredicateExpression {

	/**
	 * List of disjuncts.
	 */
	private List<PredicateExpression> predicates;

	/**
	 * Constructs a new disjunction.
	 * 
	 * @param predicates
	 *            list of disjuncts
	 */
	public Disjunction(List<PredicateExpression> predicates) {
		this.predicates = predicates;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Nullable
	public List<Scope> evaluate(Scope s, Monitor m) {

		List<Scope> scopes = Arrays.asList(s);
		Queue<PredicateExpression> queue = new LinkedList<PredicateExpression>(predicates);
		PredicateExpression P = null;
		List<Scope> extension = new ArrayList<Scope>();

		while (!queue.isEmpty()) {
			P = queue.poll();
			List<Scope> list = P.evaluate(s, m);
			if (list == null)
				m.add("error in predicate");
			else if (list.equals(s))
				continue;
			else
				extension.addAll(list);

			if (!extension.isEmpty()) {
				scopes = extension;
			}
		}

		return scopes;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Identifier> getVariables() {
		Set<Identifier> vars = new HashSet<Identifier>();
		for (PredicateExpression P : predicates)
			vars.addAll(P.getVariables());
		return vars;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		ST st = new ST("<predicates; separator=\" || \">");
		st.add("predicates", predicates);
		return st.render();
	}
}
