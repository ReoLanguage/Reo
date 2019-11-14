package nl.cwi.reo.interpret.components;

import java.util.List;
import java.util.Objects;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.instances.Instance;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.sets.SetAtom;
import nl.cwi.reo.interpret.sets.SetExpression;
import nl.cwi.reo.interpret.signatures.Signature;
import nl.cwi.reo.interpret.signatures.SignatureExpression;
import nl.cwi.reo.interpret.values.Value;
import nl.cwi.reo.util.Monitor;

// TODO: Auto-generated Javadoc
/**
 * A Reo component definition.
 */
public final class Component implements Value {

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
	private final SetExpression set;

	/**
	 * Constructs a new component definition.
	 *
	 * @param scope
	 *            local scope containing dependencies of this component
	 *            definition
	 * @param sign
	 *            parameters and nodes of this component definition
	 * @param set
	 *            implementation of this component definition
	 */
	public Component(Scope scope, SignatureExpression sign, SetExpression set) {
		this.scope = scope;
		this.sign = sign;
		this.set = set;
	}

	/**
	 * Instantiates a Reo connector from this component definition. If the list
	 * of ports is null, then the component is instantiated on the set of ports
	 * used in the defining interface.
	 *
	 * @param values
	 *            parameter values
	 * @param ports
	 *            ports in interface
	 * @param m
	 *            the m
	 * @return a list of instances and unifications.
	 */
	@Nullable
	public Instance instantiate(Scope values, @Nullable List<Port> ports, Monitor m) {
		Signature signature = sign.evaluate(values, ports, m);
		if (signature == null) // || m.hasErrors() && !(set instanceof SetAtom ))
			return null;
		scope.putAll(signature.getAssignments());
		Instance i = set.evaluate(scope, m);
//		if(m.hasErrors())
//			signature = sign.evaluate(values, ports, m, scope);
		if (i == null)
			return null;
		return i.reconnect(signature.getInterface());
	}

	public SetExpression getSet() {
		return set;
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
	public boolean equals(@Nullable Object other) {
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
