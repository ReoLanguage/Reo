package nl.cwi.reo.interpret.terms;

import java.util.Arrays;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.components.ComponentExpression;
import nl.cwi.reo.interpret.connectors.Semantics;
import nl.cwi.reo.util.Monitor;

public class TermComponent<T extends Semantics<T>> implements TermsExpression {

	private ComponentExpression<T> component;
	
	public TermComponent(ComponentExpression<T> component){
		this.component=component;
	}
	
	@Override
	public Terms evaluate(Scope s, Monitor m) {
		Terms terms = new Terms(Arrays.asList(this.component.evaluate(s, m)));
		return terms;
	}
}
