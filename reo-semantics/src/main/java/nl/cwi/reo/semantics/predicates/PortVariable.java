package nl.cwi.reo.semantics.predicates;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Scope;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.ports.PortType;
import nl.cwi.reo.interpret.typetags.TypeTag;
import nl.cwi.reo.interpret.values.DecimalValue;
import nl.cwi.reo.interpret.values.Value;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.util.Monitor;

// TODO: Auto-generated Javadoc
/**
 * A variable the represents the data observed at a port.
 */
public class PortVariable implements Variable {

	/**
	 * Flag for string template.
	 */
	public static final boolean node = true;

	/**
	 * Flag for string template.
	 */
	private final Port p;
	
	/**
	 * Free variables in this formula.
	 */
	private final Set<Variable> freeVars;

	/**
	 * Constructs a new port variable from a given port.
	 * 
	 * @param p
	 *            port
	 */
	public PortVariable(Port p) {
		this.p = p;
		this.freeVars = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(this)));
	}

	/**
	 * Gets the port of this port variable.
	 * 
	 * @return the port of this variable.
	 */
	public Port getPort() {
		return p;
	}

	/**
	 * Checks if the port of this variable is an input port.
	 * 
	 * @return true if the port is an input port
	 */
	public boolean isInput() {
		return p.isInput();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Nullable
	public TypeTag getTypeTag() {
		return this.p.getTypeTag();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasOutputPorts() {
		return p.getType() == PortType.OUT;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getName() {
		return p.getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Term rename(Map<Port, Port> links) {
		Port b = links.get(p);
		if (b != null)
			return new PortVariable(b);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Term substitute(Map<Variable, Term> map) {
		Term t = map.get(this);
		if (t != null)
			return t;
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Variable> getFreeVariables() {
		return freeVars;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return p.getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(@Nullable Object other) {
		if (other == null)
			return false;
		if (other == this)
			return true;
		if (!(other instanceof PortVariable))
			return false;
		PortVariable p = (PortVariable) other;
		return Objects.equals(this.p, p.p);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.p);
	}

	/**
	 * Evaluates this port variable. Currently, it updates only if a decimal
	 * value is assigned.
	 */
	@Override
	public @Nullable Term evaluate(Scope s, Monitor m) {
		Value v = s.get(new Identifier(p.getName()));
		if (v instanceof DecimalValue)
			return new DecimalTerm(((DecimalValue) v).getValue());
		// TODO include other data types.
		return this;
	}
}
