package nl.cwi.reo.interpret.instances;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.components.ComponentExpression;
import nl.cwi.reo.interpret.connectors.Semantics;
import nl.cwi.reo.interpret.ports.PortListExpression;
import nl.cwi.reo.interpret.terms.ListExpression;
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
	public Instance<T> evaluate(Scope s, Monitor m) {
		return component.evaluate(s, m).instantiate(values.evaluate(s, m), ports.evaluate(s, m), m);
	}

}
