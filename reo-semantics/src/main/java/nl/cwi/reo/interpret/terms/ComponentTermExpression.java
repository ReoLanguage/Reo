package nl.cwi.reo.interpret.terms;

import java.util.Arrays;
import java.util.List;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.components.ComponentExpression;
import nl.cwi.reo.semantics.Semantics;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of a component definition expression as a term.
 * @param <T> Reo semantics type
 */
public class ComponentTermExpression<T extends Semantics<T>> implements TermExpression {

	/**
	 * Component definition.
	 */
	private ComponentExpression<T> component;
	
	/**
	 * Constructs a new component definition term
	 * @param component		component definition
	 */
	public ComponentTermExpression(ComponentExpression<T> component){
		this.component=component;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Term> evaluate(Scope s, Monitor m) {
		return Arrays.asList(this.component.evaluate(s, m));
	}
}
