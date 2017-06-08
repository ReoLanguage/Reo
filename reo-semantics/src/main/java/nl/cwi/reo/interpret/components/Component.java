package nl.cwi.reo.interpret.components;

import java.util.List;
import java.util.Objects;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.instances.Instance;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.sets.SetExpression;
import nl.cwi.reo.interpret.signatures.Signature;
import nl.cwi.reo.interpret.signatures.SignatureExpression;
import nl.cwi.reo.interpret.values.Value;
import nl.cwi.reo.semantics.Semantics;
import nl.cwi.reo.util.Monitor;

/**
 * A component definition.
 * 
 * @param <T>
 *            Reo semantics type
 */
public final class Component<T extends Semantics<T>> implements Value {

	/**
	 * Dependencies of this component definition.
	 */
	private final Scope scope;

	/**
	 * Parameters and nodes of this component definition.
	 */
	private final SignatureExpression sign;

	/**
	 * Set of this component definition.
	 */
	private final SetExpression<T> set;

	/**
	 * Constructs a new component definition
	 * 
	 * @param scope
	 *            local scope containing dependencies of this component
	 *            definition
	 * @param sign
	 *            parameters and nodes of this component definition
	 * @param set
	 *            implementation of this component definition
	 */
	public Component(Scope scope, SignatureExpression sign, SetExpression<T> set) {
		this.scope = scope;
		this.sign = sign;
		this.set = set;
	}

	/**
	 * Instantiates a Reo connector from this component definition.
	 * 
	 * @param values
	 *            parameter values
	 * @param ports
	 *            ports in interface
	 * @return a list of instances and unifications.
	 */
	@Nullable
	public Instance<T> instantiate(List<?> values, @Nullable List<Port> ports, Monitor m) {
		Signature signature = sign.evaluate(values, ports, m);
		if (signature == null)
			return null;
		scope.putAll(signature.getAssignments());
		Instance<T> i = set.evaluate(scope, m);
		if (i == null)
			return null;
		return i.reconnect(signature.getInterface());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "" + sign + set;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object other) {
		/*
		 * TODO implement a more sophisticated equality that returns true, only
		 * if calls to their respective instantiate method return equal
		 * instances.
		 */
		return this == other;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(scope, sign, set);
	}

}
