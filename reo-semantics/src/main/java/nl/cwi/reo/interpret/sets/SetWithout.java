package nl.cwi.reo.interpret.sets;

import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.instances.Instance;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.semantics.Semantics;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of short circuit subtraction.
 * 
 * @param <T>
 *            Reo semantics type
 */
public final class SetWithout<T extends Semantics<T>> implements SetExpression<T> {
	
	/**
	 * Component name.
	 */
	private final String name;

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
	 * 
	 * @param name
	 *            component name
	 * @param first
	 *            first set
	 * @param second
	 *            second set
	 */
	public SetWithout(String name, SetExpression<T> first, SetExpression<T> second) {
		this.name = name;
		this.first = first;
		this.second = second;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Nullable
	public String getName() {
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Nullable
	public Instance<T> evaluate(Scope s, Monitor m) {
		Instance<T> i1 = first.evaluate(s, m);
		Instance<T> i2 = second.evaluate(s, m);

		// TODO : without set builder
		if (!i1.getConnector().isEmpty())
			return i1;
		return i2;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Identifier> getVariables() {
		Set<Identifier> union = first.getVariables();
		union.addAll(second.getVariables());
		return union;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return first + " - " + second;
	}

	@Override
	public boolean canEvaluate(Set<Identifier> deps) {
		// TODO Auto-generated method stub
		return false;
	}

}