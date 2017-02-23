package nl.cwi.reo.interpret.components;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.connectors.Semantics;
import nl.cwi.reo.interpret.connectors.SourceCode;
import nl.cwi.reo.interpret.connectors.AtomicReoConnector;
import nl.cwi.reo.interpret.connectors.ReoConnector;
import nl.cwi.reo.interpret.signatures.Signature;
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
	public ReoConnector<T> evaluate(Scope s, Monitor m) {
		Signature signature = sign.evaluate(s, m);
		return new AtomicReoConnector<T>(atom, source, signature.getInterface());
	}

}
