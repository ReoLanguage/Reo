package nl.cwi.reo.interpret.components;

import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.sets.SetExpression;
import nl.cwi.reo.interpret.signatures.SignatureExpression;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.util.Monitor;

/**
 * Interpretation of an atomic component definition.
 */
public final class ComponentDefinition implements ComponentExpression {

	/**
	 * Parameters and nodes of this component definition.
	 */
	private final SignatureExpression sign;

	/**
	 * Set of this component definition.
	 */
	private final SetExpression set;

	/**
	 * Constructs a new component definition.
	 * 
	 * @param sign
	 *            signature
	 * @param set
	 *            composite definition.
	 */
	public ComponentDefinition(SignatureExpression sign, SetExpression set) {
		this.sign = sign;
		this.set = set;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Nullable
	public Component evaluate(Scope s, Monitor m) {
		Set<Identifier> deps = s.getKeys();
		deps.addAll(sign.getParams());
		if (set.canEvaluate(deps))
			return new Component(s, sign, set);
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Identifier> getVariables() {
		return set.getVariables();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "" + sign + set;
	}

}
