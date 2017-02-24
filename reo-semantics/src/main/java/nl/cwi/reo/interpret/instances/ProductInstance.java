package nl.cwi.reo.interpret.instances;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.connectors.ReoConnectorComposite;
import nl.cwi.reo.interpret.connectors.Semantics;
import nl.cwi.reo.interpret.terms.Term;
import nl.cwi.reo.interpret.terms.TermExpression;
import nl.cwi.reo.interpret.values.StringValue;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.util.Location;
import nl.cwi.reo.util.Monitor;

public final class ProductInstance<T extends Semantics<T>> implements InstanceExpression<T> {

	/**
	 * Composition operator name.
	 */
	private final TermExpression operator;
	
	/**
	 * First instance.
	 */
	private final InstanceExpression<T> first;
	
	/**
	 * Second instance.
	 */
	private final InstanceExpression<T> second;
	
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
	public ProductInstance(TermExpression operator, InstanceExpression<T> first, InstanceExpression<T> second, Location location) {
		this.operator = operator;
		this.first = first;
		this.second = second;
		this.location = location;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Instance<T> evaluate(Scope s, Monitor m) {
		Instance<T> i1 = first.evaluate(s, m);
		Instance<T> i2 = second.evaluate(s, m);
		List<Term> op = operator.evaluate(s, m);
		
		if (op.get(0) instanceof StringValue) {
			String co = ((StringValue)op.get(0)).getValue();
		
			ReoConnectorComposite<T> R = new ReoConnectorComposite<T>(co, Arrays.asList(i1.getConnector(), i2.getConnector()));
			
			// TODO before adding setId to set, check if setId intersects with existing element of set.
			Set<Set<Identifier>> set = i1.getUnifications();
			for(Set<Identifier> setId : i2.getUnifications())
				set.add(setId);
			
			return new Instance<T>(R, set);
		}
		
		m.add(location, "Composition operator " + operator + " must be of type string.");
		return null;
	}

}
