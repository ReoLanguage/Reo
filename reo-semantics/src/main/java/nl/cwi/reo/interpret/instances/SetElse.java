package nl.cwi.reo.interpret.instances;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.connectors.CompositeReoComponent;
import nl.cwi.reo.interpret.connectors.ReoComponent;
import nl.cwi.reo.interpret.connectors.Semantics;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of short circuit addition.
 * @param <T> Reo semantics type
 */
public final class SetElse<T extends Semantics<T>> implements InstancesExpression<T> {
	
	/**
	 * First set.
	 */
	private final SetExpression<T> first;

	/**
	 * Second set.
	 */
	private final SetExpression<T> second;
	
	/**
	 * Short circuit addition of two sets of constraints.
	 * @param first		first set
	 * @param second	second set
	 */
	public SetElse(SetExpression<T> first, SetExpression<T> second) {
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

		if(!i1.getConnector().isEmpty()){
			return i1;	
		}
		else
			return i2;		
	}

}
