package nl.cwi.reo.interpret.instances;

import java.util.ArrayList;
import java.util.List;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.connectors.ReoComponent;
import nl.cwi.reo.interpret.connectors.Semantics;
import nl.cwi.reo.interpret.predicates.Predicate;
import nl.cwi.reo.interpret.predicates.PredicateExpression;
import nl.cwi.reo.interpret.terms.Term;
import nl.cwi.reo.interpret.terms.Terms;
import nl.cwi.reo.interpret.terms.TermsExpression;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of a set of constraints.
 * @param <T> Reo semantics type
 */
public final class SetExpression<T extends Semantics<T>> implements InstancesExpression<T> {

	/**
	 * Composition operator.
	 */
	private TermsExpression operator;
	
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
	public SetExpression(TermsExpression operator, List<InstancesExpression<T>> elements, PredicateExpression predicate){
		this.operator = operator;
		this.elements = elements;
		this.predicate = predicate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Instances<T> evaluate(Scope s, Monitor m) {
		Terms op = operator.evaluate(s, m);
		Predicate p = predicate.evaluate(s, m);

		List<ReoComponent<T>> instances = new ArrayList<ReoComponent<T>>();
		for(InstancesExpression<T> i : elements){
			instances.addAll(i.evaluate(s, m).getConnector());
			
		}
//		Instances<T> i = new Instances<T>();		
		return null;
	}

}
