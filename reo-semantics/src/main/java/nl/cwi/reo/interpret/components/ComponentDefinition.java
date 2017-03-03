package nl.cwi.reo.interpret.components;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.sets.SetExpression;
import nl.cwi.reo.interpret.signatures.SignatureExpression;
import nl.cwi.reo.semantics.Semantics;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of an atomic component definition.
 * 
 * @param <T>
 *            Reo semantics type
 */
public final class ComponentDefinition<T extends Semantics<T>> implements ComponentExpression<T> {

	/**
	 * Parameters and nodes of this component definition.
	 */
	private final SignatureExpression sign;

	/**
	 * Set of this component definition.
	 */
	private final SetExpression<T> set;

	/**
	 * Constructs a new component definition.
	 * 
	 * @param sign
	 *            signature
	 * @param set
	 *            composite definition.
	 */
	public ComponentDefinition(SignatureExpression sign, SetExpression<T> set) {
		this.sign = sign;
		this.set = set;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Nullable
	public Component<T> evaluate(Scope s, Monitor m) {
		return new Component<T>(s, sign, set);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "" + sign + set;
	}
}
