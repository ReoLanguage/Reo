package nl.cwi.reo.interpret.instances;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.components.Component;
import nl.cwi.reo.interpret.components.ComponentExpression;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.ports.PortListExpression;
import nl.cwi.reo.interpret.terms.ListExpression;
import nl.cwi.reo.interpret.terms.Term;
import nl.cwi.reo.interpret.values.Value;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.util.Monitor;

// TODO: Auto-generated Javadoc
/**
 * Interpretation of an atomic component instance.
 */
public final class ComponentInstance implements InstanceExpression {

	/**
	 * Component definition.
	 */
	private final ComponentExpression component;

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
	 * 
	 * @param component
	 *            component definition
	 * @param values
	 *            parameter values
	 * @param ports
	 *            interface ports
	 */
	public ComponentInstance(ComponentExpression component, ListExpression values, PortListExpression ports) {
		this.component = component;
		this.values = values;
		this.ports = ports;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Nullable
	public Instance evaluate(Scope s, Monitor m) {
		List<Term> v = values.evaluate(s, m);
		List<Port> p = ports.evaluate(s, m);
		Component c = component.evaluate(s, m);
		Scope param =  new Scope();
		for(int i=0; i< v.size();i++) {
			if(v.get(i) instanceof Value)
			param.put(new Identifier(
								// c.getSet().getName() + "." + 
									Integer.toString(i)), (Value)v.get(i));
		}
		if (param ==null ||  p == null || c == null)
			return null;
		return c.instantiate(param, p, m);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Identifier> getVariables() {
		Set<Identifier> vars = new HashSet<Identifier>();
		vars.addAll(component.getVariables());
		vars.addAll(values.getVariables());
		return vars;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "" + component + values + ports;
	}

}
