package nl.cwi.reo.interpret.terms;

import java.util.Arrays;
import java.util.List;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.instances.Instance;
import nl.cwi.reo.interpret.instances.InstanceExpression;
import nl.cwi.reo.semantics.Semantics;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of an instance as a term.
 * 
 * @param <T>
 *            Reo semantics type
 */
public class InstanceTermExpression<T extends Semantics<T>> implements TermExpression {

	/**
	 * Component instance.
	 */
	private InstanceExpression<T> instance;

	/**
	 * Constructs a new component instance term.
	 * 
	 * @param instance
	 *            component instance
	 */
	public InstanceTermExpression(InstanceExpression<T> instance) {
		this.instance = instance;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Nullable
	public List<Term> evaluate(Scope s, Monitor m) {
		Instance<T> inst = this.instance.evaluate(s, m);
		if (inst == null)
			return null;
		return Arrays.asList(inst);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return instance.toString();
	}
}
