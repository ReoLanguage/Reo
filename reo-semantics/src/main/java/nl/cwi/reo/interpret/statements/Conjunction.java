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
import nl.cwi.reo.util.Message;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of a conjunction.
 */
public final class Conjunction implements PredicateExpression {

	/**
	 * List of conjuncts.
	 */
	private List<PredicateExpression> predicates;

	/**
	 * Constructs a new conjunction.
	 * 
	 * @param predicates
	 *            list of conjuncts.
	 */
	public Conjunction(List<PredicateExpression> predicates) {
		this.predicates = predicates;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Nullable
	public List<Scope> evaluate(Scope s, Monitor m) {

		List<Scope> scopes = Arrays.asList(s);
		Queue<PredicateExpression> stack = new LinkedList<PredicateExpression>(predicates);
		PredicateExpression P = null;
		List<Scope> extension = new ArrayList<Scope>();
		List<Scope> tmpList = new ArrayList<Scope>();
		int counter = 0;

		Monitor localm = new Monitor();

		while (!stack.isEmpty()) {

			P = stack.poll();
			if (P != null) {
				for (Scope si : scopes) {
					List<Scope> list = P.evaluate(si, localm);
					if (list == null) {
						counter++;
						stack.add(P);
						continue;
					} else if (list.equals(extension)) {
						continue;
					} else {
						counter = 0;
						tmpList.addAll(list);
					}
				}
			}
			extension = tmpList;
			tmpList = new ArrayList<Scope>();

			if (!extension.isEmpty()) {
				scopes = new ArrayList<Scope>();
				scopes.addAll(extension);
			}

			if (counter > stack.size()) {
				for (Message msg : localm.getMessages()) {
					m.add(msg);
				}
				// m.print();
				break;
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
		ST st = new ST("<predicates; separator=\" && \">");
		st.add("predicates", predicates);
		return st.render();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Nullable
	public Set<Identifier> getDefinedVariables(Set<Identifier> defns) {
		Set<Identifier> vars = new HashSet<Identifier>(defns);
		Queue<PredicateExpression> stack = new LinkedList<PredicateExpression>(predicates);
		PredicateExpression P = null;
		int counter = 0;

		while (!stack.isEmpty()) {
			P = stack.poll();
			if (P != null) {
				Set<Identifier> defnP = P.getDefinedVariables(vars);
				if (defnP == null) {
					counter++;
					stack.add(P);
				} else {
					counter = 0;
					vars.addAll(defnP);
				}
			}
			if (counter > stack.size())
				return null;
		}

		return vars;
	}

}
