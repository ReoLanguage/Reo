package nl.cwi.reo.interpret.sets;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.connectors.Semantics;
import nl.cwi.reo.interpret.instances.Instances;
import nl.cwi.reo.interpret.instances.InstancesExpression;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of short circuit addition.
 * @param <T> Reo semantics type
 */
public final class SetElse<T extends Semantics<T>> implements InstancesExpression<T> {
	
	/**
	 * First set.
	 */
	private final SetComposite<T> first;

	/**
	 * Second set.
	 */
	private final SetComposite<T> second;

	/**
	 * Short circuit addition of two sets of constraints.
	 * @param first		first set
	 * @param second	second set
	 */
	public SetElse(SetComposite<T> first, SetComposite<T> second) {
		this.first = first;
		this.second = second;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Instances<T> evaluate(Scope s, Monitor m) {
		Instances<T> insts = first.evaluate(s, m);
		if (insts.getConnector().isEmpty())
			return second.evaluate(s, m);
		return insts;		
	}

}
