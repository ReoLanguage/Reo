package nl.cwi.reo.interpret.sets;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.instances.Instance;
import nl.cwi.reo.semantics.Semantics;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of short circuit addition.
 * @param <T> Reo semantics type
 */
public final class SetElse<T extends Semantics<T>> implements SetExpression<T> {
	
	/**
	 * First set.
	 */
	private final SetExpression<T> first;

	/**
	 * Second set.
	 */
	private final SetExpression<T> second;

	/**
	 * Short circuit addition of two sets of constraints.
	 * @param first		first set
	 * @param second	second set
	 */
	public SetElse(SetExpression<T> first, SetExpression<T> second) {
		this.first = first;
		this.second = second;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Nullable
	public Instance<T> evaluate(Scope s, Monitor m) {
		Instance<T> insts = first.evaluate(s, m);
		if (insts == null) return null;
		if (insts.getConnector().isEmpty())
			return second.evaluate(s, m);
		return insts;		
	}

}
