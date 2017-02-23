package nl.cwi.reo.interpret.instances;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.connectors.CompositeReoComponent;
import nl.cwi.reo.interpret.connectors.ReoComponent;
import nl.cwi.reo.interpret.connectors.Semantics;
import nl.cwi.reo.interpret.terms.Terms;
import nl.cwi.reo.interpret.terms.TermsExpression;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.util.Monitor;

public final class InstanceComposite<T extends Semantics<T>> implements InstancesExpression<T> {

	/**
	 * Composition operator name.
	 */
	private final TermsExpression operator;
	
	/**
	 * First instance.
	 */
	private final InstancesExpression<T> first;
	
	/**
	 * Second instance.
	 */
	private final InstancesExpression<T> second;
		
	/**
	 * Constructs a new composition of instances.
	 * @param operator	composition operator
	 * @param first		first instance
	 * @param second	second instance
	 */
	public InstanceComposite(TermsExpression operator, InstancesExpression<T> first, InstancesExpression<T> second) {
		this.operator = operator;
		this.first = first;
		this.second = second;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Instances<T> evaluate(Scope s, Monitor m) {
		Instances<T> i1 = first.evaluate(s, m);
		Instances<T> i2 = second.evaluate(s, m);
		Terms op = operator.evaluate(s, m);
		
		List<ReoComponent<T>> list = new ArrayList<ReoComponent<T>>(i1.getConnector());
		list.addAll(i2.getConnector());
		CompositeReoComponent<T> c = new CompositeReoComponent<T>(op.toString(),list);
		
		Set<Set<Identifier>> set = i1.getUnifications();
		for(Set<Identifier> setId : i2.getUnifications())
			set.add(setId);
		
		return new Instances<T>(Arrays.asList(c),set);
	}

}
