package nl.cwi.reo.semantics.rba;

import java.util.Map;
import java.util.Objects;

import org.checkerframework.checker.nullness.qual.Nullable;
import nl.cwi.reo.interpret.ports.Port;

// TODO: Auto-generated Javadoc
/**
 * A node in a constraint hypergraph that represents a port.
 */
public class PortNode {

	/**
	 * Port of this node.
	 */
	private Port p;

	/**
	 * Constructs a constraint hypergraph node from a port.
	 * 
	 * @param p
	 *            port
	 */
	public PortNode(Port p) {
		this.p = p;
	}

	/**
	 * Renames the port of this node to another port.
	 * 
	 * @param p
	 *            other port
	 * @return node with other port
	 */
	public PortNode setPort(Port p) {
		this.p = p;
		return this;
	}

	/**
	 * Gets the port of this node.
	 * 
	 * @return port of this node.
	 */
	public Port getPort() {
		return p;
	}

	/**
	 * Renames the port of this node.
	 * 
	 * @param links
	 *            map that assigns a new port to each old port.
	 * @return node with new port.
	 */
	public PortNode rename(Map<Port, Port> links) {
		Port newPort = links.get(p);
		if (newPort != null)
			p = p.rename(newPort.getName());
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return p.toString();
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
		if (!(other instanceof PortNode))
			return false;
		PortNode port = (PortNode) other;
		return Objects.equals(this.p, port.p);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.p);
	}
}
