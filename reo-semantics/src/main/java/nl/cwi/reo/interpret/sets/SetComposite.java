package nl.cwi.reo.interpret.sets;

import java.util.List;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.connectors.Semantics;
import nl.cwi.reo.interpret.instances.Instances;
import nl.cwi.reo.interpret.instances.InstancesExpression;
import nl.cwi.reo.interpret.predicates.PredicateExpression;
import nl.cwi.reo.interpret.terms.TermExpression;
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
	private List<InstancesExpression<T>> elements;
	
	/**
	 * Predicate of this set.
	 */
	private PredicateExpression predicate;
	
	/**
	 * Constructs a new set of component instances.
	 * @param operator		composition operator
	 * @param elements		elements in this set
	 * @param predicate		predicate of this set
	 */
	public SetComposite(TermExpression operator, List<InstancesExpression<T>> elements, PredicateExpression predicate){
		this.operator = operator;
		this.elements = elements;
		this.predicate = predicate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Instances<T> evaluate(Scope s, Monitor m) {
//		Predicate p = predicate.evaluate(s, m);
//
//		List<ReoConnector<T>> instances = new ArrayList<ReoConnector<T>>();
//		for(InstancesExpression<T> i : elements){
//			instances.addAll(i.evaluate(s, m).getConnector());
//			
//		}
//		Instances<T> i = new Instances<T>();		
		return null;
	}

}
