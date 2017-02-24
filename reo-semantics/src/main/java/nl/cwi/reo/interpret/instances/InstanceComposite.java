package nl.cwi.reo.interpret.instances;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.connectors.CompositeReoConnector;
import nl.cwi.reo.interpret.connectors.Semantics;
import nl.cwi.reo.interpret.terms.Term;
import nl.cwi.reo.interpret.terms.TermExpression;
import nl.cwi.reo.interpret.values.StringValue;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.util.Location;
import nl.cwi.reo.util.Monitor;

public final class InstanceComposite<T extends Semantics<T>> implements InstancesExpression<T> {

	/**
	 * Composition operator name.
	 */
	private final TermExpression operator;
	
	/**
	 * First instance.
	 */
	private final InstancesExpression<T> first;
	
	/**
	 * Second instance.
	 */
	private final InstancesExpression<T> second;
	
	/**
	 * Location of this instance in Reo source file.
	 */
	private final Location location;
		
	/**
	 * Constructs a new composition of instances.
	 * @param operator	composition operator
	 * @param first		first instance
	 * @param second	second instance
	 */
	public InstanceComposite(TermExpression operator, InstancesExpression<T> first, InstancesExpression<T> second, Location location) {
		this.operator = operator;
		this.first = first;
		this.second = second;
		this.location = location;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Instances<T> evaluate(Scope s, Monitor m) {
		Instances<T> i1 = first.evaluate(s, m);
		Instances<T> i2 = second.evaluate(s, m);
		List<Term> op = operator.evaluate(s, m);
		
		if (op.get(0) instanceof StringValue) {
			String co = ((StringValue)op.get(0)).getValue();
		
			CompositeReoConnector<T> R = new CompositeReoConnector<T>(co, Arrays.asList(i1.getConnector(), i2.getConnector()));
			
			// TODO before adding setId to set, check if setId intersects with existing element of set.
			Set<Set<Identifier>> set = i1.getUnifications();
			for(Set<Identifier> setId : i2.getUnifications())
				set.add(setId);
			
			return new Instances<T>(R, set);
		}
		
		m.add(location, "Composition operator " + operator + " must be of type string.");
		return null;
	}

}
