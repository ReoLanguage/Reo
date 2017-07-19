package nl.cwi.reo.interpret.sets;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.stringtemplate.v4.ST;

import nl.cwi.reo.interpret.Interpretable;
import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.connectors.ReoConnector;
import nl.cwi.reo.interpret.connectors.ReoConnectorComposite;
import nl.cwi.reo.interpret.instances.Instance;
import nl.cwi.reo.interpret.instances.InstanceExpression;
import nl.cwi.reo.interpret.statements.TruthValue;
import nl.cwi.reo.interpret.statements.PredicateExpression;
import nl.cwi.reo.interpret.statements.Relation;
import nl.cwi.reo.interpret.terms.Term;
import nl.cwi.reo.interpret.terms.TermExpression;
import nl.cwi.reo.interpret.values.StringValue;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.util.Location;
import nl.cwi.reo.util.Monitor;

// TODO: Auto-generated Javadoc
/**
 * Interpretation of a set of constraints.
 * 
 * @param <T>
 *            Reo semantics type
 */
public final class SetComposite<T extends Interpretable<T>> implements SetExpression<T> {

	/**
	 * Component name.
	 */
	@Nullable
	private final String name;

	/**
	 * Composition operator.
	 */
	private TermExpression operator;

	/**
	 * Elements of this set.
	 */
	private List<InstanceExpression<T>> elements;

	/**
	 * Predicate of this set.
	 */
	private PredicateExpression predicate;

	/**
	 * Location of this instance in Reo source file.
	 */
	@Nullable
	private final Location location;

	/**
	 * Constructs an empty set expression.
	 */
	public SetComposite() {
		this.name = null;
		this.operator = new StringValue("");
		this.elements = new ArrayList<InstanceExpression<T>>();
		this.predicate = new TruthValue(true);
		this.location = null;
	}

	/**
	 * Constructs a new set of component instances.
	 * 
	 * @param name
	 *            component name
	 * @param operator
	 *            composition operator
	 * @param elements
	 *            elements in this set
	 * @param predicate
	 *            predicate of this set
	 * @param location
	 *            location in Reo source file
	 */
	public SetComposite(@Nullable String name, TermExpression operator, List<InstanceExpression<T>> elements,
			PredicateExpression predicate, @Nullable Location location) {
		if (operator == null || elements == null || predicate == null)
			throw new NullPointerException();
		this.name = name;
		this.operator = operator;
		this.predicate = predicate;
		this.elements = elements;
		this.location = location;
	}

	/**
	 * {@inheritDoc}
	 */
	@Nullable
	public String getName() {
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Nullable
	public Instance<T> evaluate(Scope s, Monitor m) {

		List<Term> t = this.operator.evaluate(s, m);
		if (t == null || t.isEmpty() || !(t.get(0) instanceof StringValue)) {
			m.add(location, "Composition operator " + operator + " must be of type string.");
			return null;
		}

		String operator = ((StringValue) t.get(0)).getValue();
		List<ReoConnector<T>> components = new ArrayList<ReoConnector<T>>();
		Set<Set<Identifier>> unifications = new HashSet<Set<Identifier>>();

		List<Scope> scopes = predicate.evaluate(s, m);
		if (scopes == null)
			return null;

		for (Scope si : scopes) {
			for (InstanceExpression<T> e : elements) {
				Instance<T> i = e.evaluate(si, m);
				if (i != null) {
					components.add(i.getConnector());
					unifications.addAll(i.getUnifications());
				}
			}
		}

		simplify(unifications);

		return new Instance<T>(ReoConnectorComposite.compose(operator, components), unifications);
	}

	/**
	 * Unifies intersecting subsets.
	 * 
	 * @param partition
	 *            set of subsets
	 */
	private void simplify(Set<Set<Identifier>> partition) {
		for (Set<Identifier> x : partition) {
			for (Set<Identifier> y : partition) {
				if (y != x && (new HashSet<Identifier>(y)).removeAll(x)) {
					y.addAll(x);
					partition.remove(x);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Identifier> getVariables() {
		Set<Identifier> vars = new HashSet<Identifier>();
		for (InstanceExpression<T> I : elements)
			vars.addAll(I.getVariables());
		if (predicate instanceof Relation) {
			for (Identifier i : ((Relation) predicate).getLocalVariables())
				if (vars.contains(i))
					vars.remove(i);
				else
					vars.add(i);
			vars.addAll(((Relation) predicate).getGlobalVariables());
		}
		// TODO : implement getGlobalVariable and getLocalVariable for all
		// predicates
		else
			vars.removeAll(predicate.getVariables());
		return vars;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		ST st = new ST("<operator>{\n  <elements; separator=\"\n\">\n<if(predicate)>|\n  <predicate>\n<endif>}");
		st.add("operator", operator);
		st.add("elements", elements);
		if (!(predicate instanceof TruthValue) && !((TruthValue) predicate).equals(new TruthValue(true)))
			st.add("predicate", predicate);
		return st.render();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nl.cwi.reo.interpret.sets.SetExpression#canEvaluate(java.util.Set)
	 */
	@Override
	public boolean canEvaluate(Set<Identifier> deps) {
		Set<Identifier> vars = new HashSet<>();
		for (InstanceExpression<T> I : elements) {
			Set<Identifier> varsI = I.getVariables();
			if (varsI != null)
				vars.addAll(varsI);
		}
		Set<Identifier> ids = predicate.getDefinedVariables(deps);
		if (ids != null)
			vars.removeAll(ids);
		return vars.isEmpty();
	}
}
