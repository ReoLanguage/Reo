package nl.cwi.reo.interpret.instances;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.connectors.Semantics;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of short circuit subtraction.
 * @param <T> Reo semantics type
 */
public final class SetWithout<T extends Semantics<T>> implements InstancesExpression<T> {
	
	/**
	 * First set.
	 */
	private final Set<T> first;

	/**
	 * Second set.
	 */
	private final Set<T> second;
	
	/**
	 * Short circuit subtraction of two sets of constraints.
	 * @param first		first set
	 * @param second	second set
	 */
	public SetWithout(Set<T> first, Set<T> second) {
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