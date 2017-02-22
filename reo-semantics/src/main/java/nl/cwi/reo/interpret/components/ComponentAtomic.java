package nl.cwi.reo.interpret.components;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.connectors.Semantics;
import nl.cwi.reo.interpret.connectors.SourceCode;
import nl.cwi.reo.interpret.connectors.ReoComponent;
import nl.cwi.reo.interpret.signatures.SignatureExpression;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of an atomic component definition.
 * @param <T> Reo semantics type
 */
public final class ComponentAtomic<T extends Semantics<T>> implements ComponentExpression<T> {
	
	private final SignatureExpression sign;
	
	private final T atom;
	
	private final SourceCode source;
	
	public ComponentAtomic(SignatureExpression sign,T atom, SourceCode source) {
		this.sign = sign;
		this.atom = atom;
		this.source = source;
	}

	@Override
	public ReoComponent<T> evaluate(Scope s, Monitor m) {
		// TODO Auto-generated method stub
		return null;
	}

}
