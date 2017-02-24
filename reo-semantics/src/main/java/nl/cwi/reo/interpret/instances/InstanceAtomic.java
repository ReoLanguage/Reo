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
public final class InstanceAtomic<T extends Semantics<T>> implements InstancesExpression<T> {

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
	public InstanceAtomic(ComponentExpression<T> component, ListExpression values, PortListExpression ports) {
		this.component = component;
		this.values = values;
		this.ports = ports;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Instances<T> evaluate(Scope s, Monitor m) {
//		ReoConnector<T> reoComp = component.evaluate(s, m);
//		Terms t = values.evaluate(s, m);
//		// TODO : add t to scope;
//		List<Variable> v = ports.evaluate(s, m);
//		Set<Set<Identifier>> set = new HashSet<Set<Identifier>>();
//		Set<Identifier> setP = new HashSet<Identifier>();
//		for(Variable variable : v){
//			if(variable instanceof Port)
//				setP.add((Port) variable);
//			else
//				m.add(new Location("InstanceAtomic.java",66,13), "VariableListExpression is not evaluated as a Port");
//		}
//		set.add(setP);
//		CompositeReoConnector<T> comp = new CompositeReoConnector<T>(" ", Arrays.asList(reoComp), reoComp.getLinks());
//		return new Instances<T>(Arrays.asList(comp),set);
		return null;
	}

}
