package nl.cwi.reo.interpret.instances;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.connectors.Semantics;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of short circuit subtraction.
 * @param <T> Reo semantics type
 */
public final class SetWithout<T extends Semantics<T>> implements InstancesExpression {
	
	/**
	 * First set.
	 */
	private final SetExpression<T> first;

	/**
	 * Second set.
	 */
	private final SetExpression<T> second;
	
	/**
	 * Short circuit subtraction of two sets of constraints.
	 * @param first		first set
	 * @param second	second set
	 */
	public SetWithout(SetExpression<T> first, SetExpression<T> second) {
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