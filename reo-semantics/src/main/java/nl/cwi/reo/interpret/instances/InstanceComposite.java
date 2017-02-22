package nl.cwi.reo.interpret.instances;

import java.util.Arrays;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.connectors.Semantics;
import nl.cwi.reo.interpret.terms.Terms;
import nl.cwi.reo.interpret.terms.TermsExpression;
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
		
//		return new Instances<T>(Arrays.asList(new Connector<T>()),);
		return null;
	}

}
