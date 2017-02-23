package nl.cwi.reo.interpret.components;

import java.util.Arrays;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.connectors.AtomicReoComponent;
import nl.cwi.reo.interpret.connectors.CompositeReoComponent;
import nl.cwi.reo.interpret.connectors.Semantics;
import nl.cwi.reo.interpret.connectors.ReoComponent;
import nl.cwi.reo.interpret.instances.Instances;
import nl.cwi.reo.interpret.instances.SetExpression;
import nl.cwi.reo.interpret.signatures.Signature;
import nl.cwi.reo.interpret.signatures.SignatureExpression;
import nl.cwi.reo.interpret.terms.Terms;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of an atomic component definition.
 * @param <T> Reo semantics type
 */
public final class ComponentComposite<T extends Semantics<T>> implements ComponentExpression<T> {

	private final SignatureExpression sign;	
	
	private final SetExpression<T> set;
	
	
	public ComponentComposite(SignatureExpression sign, SetExpression<T> set) {
		this.sign=sign;
		this.set=set;
	}

	@Override
	public ReoComponent<T> evaluate(Scope s, Monitor m) {
		Signature signature = sign.evaluate(s, m);
		Instances<T> i = set.evaluate(s, m);
		Terms operator = set.getOperator(s, m);
		return new CompositeReoComponent<T>(operator.toString(),i.getConnector(),signature.getInterface());
	}

}
