package nl.cwi.reo.interpret.sets;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.connectors.ReoConnector;
import nl.cwi.reo.interpret.connectors.ReoConnectorComposite;
import nl.cwi.reo.interpret.instances.Instance;
import nl.cwi.reo.interpret.instances.InstanceExpression;
import nl.cwi.reo.interpret.statements.TruthValue;
import nl.cwi.reo.interpret.statements.PredicateExpression;
import nl.cwi.reo.interpret.terms.Term;
import nl.cwi.reo.interpret.terms.TermExpression;
import nl.cwi.reo.interpret.values.StringValue;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.semantics.Semantics;
import nl.cwi.reo.util.Location;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of a set of constraints.
 * @param <T> Reo semantics type
 */
public final class SetComposite<T extends Semantics<T>> implements SetExpression<T> {

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
	private final Location location;
	
	/**
	 * Constructs an empty set expression.
	 */
	public SetComposite() {
		this.operator = new StringValue("");
		this.elements = new ArrayList<InstanceExpression<T>>();
		this.predicate = new TruthValue(true);
		this.location = null;
	}
	
	/**
	 * Constructs a new set of component instances.
	 * @param operator		composition operator
	 * @param elements		elements in this set
	 * @param predicate		predicate of this set
	 * @param location		location in Reo source file
	 */
	public SetComposite(TermExpression operator, List<InstanceExpression<T>> elements, PredicateExpression predicate, Location location){
		if(operator==null)
			this.operator=new StringValue("");
		else
			this.operator = operator;

		if(predicate==null)
			this.predicate=new TruthValue(true);
		else
			this.predicate = predicate;
		

		this.elements = elements;
		this.location = location;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Instance<T> evaluate(Scope s, Monitor m) {
		
		List<Term> t = this.operator.evaluate(s, m);
		if (t.isEmpty() || !(t.get(0) instanceof StringValue)) {
			m.add(location, "Composition operator " + operator + " must be of type string.");
			return null;
		} 

		String operator = ((StringValue)t.get(0)).getValue();
		List<ReoConnector<T>> components = new ArrayList<ReoConnector<T>>();
		Set<Set<Identifier>> unifications = new HashSet<Set<Identifier>>();
		
		List<Scope> scopes = predicate.evaluate(s, m);
		for (Scope si : scopes) {
			for (InstanceExpression<T> e : elements) {
				Instance<T> i = e.evaluate(si, m);
				if(i!=null){
					components.add(i.getConnector());
					unifications.addAll(i.getUnifications());
				}
			}
		}
		
		simplify(unifications);
		
		return new Instance<T>(new ReoConnectorComposite<T>(operator, components), unifications);
	}

	/**
	 * Unifies intersecting subsets.
	 * @param partition		set of subsets
	 * @return Returns true, if the set of subsets changed.
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
}
