package nl.cwi.reo.interpret.instances;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.connectors.Semantics;
import nl.cwi.reo.interpret.terms.TermsExpression;
import nl.cwi.reo.util.Monitor;

public final class InstanceComposite<T extends Semantics<T>> implements InstancesExpression<T> {

	/**
	 * Composition operator name.
	 */
	private final TermsExpression operator;
	
	/**
	 * First instance.
	 */
	private final InstancesExpression<T> first;
	
	/**
	 * Second instance.
	 */
	private final InstancesExpression<T> second;
		
	/**
	 * Constructs a new composition of instances.
	 * @param operator	composition operator
	 * @param first		first instance
	 * @param second	second instance
	 */
	public InstanceComposite(TermsExpression operator, InstancesExpression<T> first, InstancesExpression<T> second) {
		this.operator = operator;
		this.first = first;
		this.second = second;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Instances<T> evaluate(Scope s, Monitor m) {
		// TODO Auto-generated method stub
		return null;
	}

}
