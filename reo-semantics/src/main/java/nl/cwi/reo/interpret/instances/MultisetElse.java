package nl.cwi.reo.interpret.instances;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.connectors.Semantics;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of short circuit addition.
 * @param <T> Reo semantics type
 */
public final class MultisetElse<T extends Semantics<T>> implements InstancesExpression<T> {
	
	/**
	 * First set.
	 */
	private final Multiset<T> first;

	/**
	 * Second set.
	 */
	private final Multiset<T> second;
	
	/**
	 * Short circuit addition of two sets of constraints.
	 * @param first		first set
	 * @param second	second set
	 */
	public MultisetElse(Multiset<T> first, Multiset<T> second) {
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
