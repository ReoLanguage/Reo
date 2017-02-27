package nl.cwi.reo.interpret.sets;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.instances.Instance;
import nl.cwi.reo.interpret.instances.InstanceExpression;
import nl.cwi.reo.semantics.Semantics;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of short circuit subtraction.
 * @param <T> Reo semantics type
 */
public final class SetWithout<T extends Semantics<T>> implements InstanceExpression<T> {

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
	public Instance<T> evaluate(Scope s, Monitor m) {
		Instance<T> i1 = first.evaluate(s, m);
		Instance<T> i2 = second.evaluate(s, m);
		
		// TODO : without set builder
		if(!i1.getConnector().isEmpty())
			return i1;	
		return i2;	
	}

}