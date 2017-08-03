package nl.cwi.reo.semantics.predicates;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.ports.PortType;
import nl.cwi.reo.interpret.typetags.TypeTag;

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
	 * Constructs a new port variable from a given port.
	 * 
	 * @param p
	 *            port
	 */
	public PortVariable(Port p) {
		this.p = p;
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
	public Term substitute(Term t, Variable x) {
		if (this.equals(x))
			return t;
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Variable> getFreeVariables() {
		return new HashSet<Variable>(Arrays.asList(this));
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
}
