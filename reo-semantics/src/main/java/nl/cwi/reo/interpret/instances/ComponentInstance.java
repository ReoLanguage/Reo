package nl.cwi.reo.interpret.instances;

import java.util.List;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.components.Component;
import nl.cwi.reo.interpret.components.ComponentExpression;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.ports.PortListExpression;
import nl.cwi.reo.interpret.terms.ListExpression;
import nl.cwi.reo.interpret.terms.Term;
import nl.cwi.reo.semantics.Semantics;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of an atomic component instance.
 * @param <T> Reo semantics type
 */
public final class ComponentInstance<T extends Semantics<T>> implements InstanceExpression<T> {

	/**
	 * Component definition.
	 */
	private final ComponentExpression<T> component;
	
	/**
	 * List of parameter values.
	 */
	private final ListExpression values;
	
	/**
	 * List of ports.
	 */
	private final PortListExpression ports;
	
	/**
	 * Constructs an atomic component instance.
	 * @param component	component definition
	 * @param values	parameter values
	 * @param ports		interface ports
	 */
	public ComponentInstance(ComponentExpression<T> component, ListExpression values, PortListExpression ports) {
		this.component = component;
		this.values = values;
		this.ports = ports;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Nullable
	public Instance<T> evaluate(Scope s, Monitor m) {
		List<Term> v = values.evaluate(s, m);
		List<Port> p = ports.evaluate(s, m);
		Component<T> c = component.evaluate(s, m);
		if (v == null || p == null || c == null) return null;
		return c.instantiate(v, p, m);
	}

}
