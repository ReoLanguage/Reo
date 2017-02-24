package nl.cwi.reo.interpret.terms;

import java.util.Arrays;
import java.util.List;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.connectors.Semantics;
import nl.cwi.reo.interpret.instances.InstancesExpression;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of an instance as a term.
 * @param <T> Reo semantics type
 */
public class InstanceTermExpression<T extends Semantics<T>> implements TermExpression {

	/**
	 * Component instance.
	 */
	private InstancesExpression<T> instance;
	
	/**
	 * Constructs a new component instance term.
	 * @param instance		component instance
	 */
	public InstanceTermExpression(InstancesExpression<T> instance) {
		this.instance = instance;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Term> evaluate(Scope s, Monitor m) {
		return Arrays.asList(this.instance.evaluate(s, m));
	}
}
