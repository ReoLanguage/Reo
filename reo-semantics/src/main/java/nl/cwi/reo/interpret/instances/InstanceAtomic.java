package nl.cwi.reo.interpret.instances;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.components.ComponentExpression;
import nl.cwi.reo.interpret.connectors.CompositeReoComponent;
import nl.cwi.reo.interpret.connectors.ReoComponent;
import nl.cwi.reo.interpret.connectors.Semantics;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.terms.TermList;
import nl.cwi.reo.interpret.terms.Terms;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.interpret.variables.Variable;
import nl.cwi.reo.interpret.variables.VariableExpression;
import nl.cwi.reo.interpret.variables.VariableListExpression;
import nl.cwi.reo.util.Location;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of an atomic component instance.
 * @param <T> Reo semantics type
 */
public final class InstanceAtomic<T extends Semantics<T>> implements InstancesExpression<T> {

	/**
	 * Component definition.
	 */
	private final ComponentExpression<T> component;
	
	/**
	 * List of parameter values.
	 */
	private final TermList values;
	
	/**
	 * List of ports.
	 */
	private final VariableListExpression ports;
	
	/**
	 * Constructs an atomic component instance.
	 * @param component	component definition
	 * @param values	parameter values
	 * @param ports		interface ports
	 */
	public InstanceAtomic(ComponentExpression<T> component, TermList values, VariableListExpression ports) {
		this.component = component;
		this.values = values;
		this.ports = ports;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Instances<T> evaluate(Scope s, Monitor m) {
		ReoComponent<T> reoComp = component.evaluate(s, m);
		Terms t = values.evaluate(s, m);
		// TODO : add t to scope;
		List<Variable> v = ports.evaluate(s, m);
		Set<Set<Identifier>> set = new HashSet<Set<Identifier>>();
		Set<Identifier> setP = new HashSet<Identifier>();
		for(Variable variable : v){
			if(variable instanceof Port)
				setP.add((Port) variable);
			else
				m.add(new Location("InstanceAtomic.java",66,13), "VariableListExpression is not evaluated as a Port");
		}
		set.add(setP);
		CompositeReoComponent<T> comp = new CompositeReoComponent<T>(" ", Arrays.asList(reoComp), reoComp.getLinks());
		return new Instances<T>(Arrays.asList(comp),set);
	}

}
