package nl.cwi.reo.interpret.components;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.connectors.Semantics;
import nl.cwi.reo.interpret.instances.Set;
import nl.cwi.reo.interpret.signatures.SignatureExpression;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of an atomic component definition.
 * @param <T> Reo semantics type
 */
public final class ComponentComposite<T extends Semantics<T>> implements ComponentExpression<T> {

	private final SignatureExpression sign;	
	
	private final Set<T> set;
	
	
	public ComponentComposite(SignatureExpression sign, Set<T> set) {
		this.sign=sign;
		this.set=set;
	}

	@Override
	public ComponentDefinition<T> evaluate(Scope s, Monitor m) {
		// TODO Auto-generated method stub
		return null;
	}

}
