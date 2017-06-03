package nl.cwi.reo.interpret.ports;

import java.util.Objects;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.typetags.TypeTag;
import nl.cwi.reo.interpret.variables.Identifier;
import nl.cwi.reo.semantics.rbautomaton.Rule;

/**
 * An identifier that is decorated with a port type, a priority type, a type
 * tag, and an indicator for visibility.
 */
public final class Port extends Identifier implements Comparable<Port> {

	/**
	 * Port type: input, output, none.
	 */
	private final PortType type;

	/**
	 * Priority type: ampersand, plus, none.
	 */
	private final PrioType prio;

	/**
	 * Type tag.
	 */
	private final TypeTag tag;

	/**
	 * Visibility.
	 */
	private final boolean visible;

	/**
	 * Constructs a port with default settings: port type and priority type are
	 * unknown, type tag is empty, and visibility is true.
	 * 
	 * @param name
	 *            port name
	 */
	public Port(String name) {
		super(name);
		this.type = PortType.NONE;
		this.prio = PrioType.NONE;
		this.tag = null;
		this.visible = true;
	}

	/**
	 * Constructs a new port.
	 * 
	 * @param name
	 *            port name
	 * @param type
	 *            port type: input/output/none
	 * @param prio
	 *            priority type: ampersand/plus/none
	 * @param tag
	 *            type tag
	 * @param hidden
	 *            visibility
	 */
	public Port(String name, PortType type, PrioType prio, TypeTag tag, boolean hidden) {
		super(name);
		if (type == null || prio == null)
			throw new NullPointerException();
		this.type = type;
		this.prio = prio;
		this.tag = tag;
		this.visible = hidden;
	}

	/**
	 * Joins this port to port origin by inheriting, if necessary, the type, tag
	 * and visibility of port origin. This method is used when ports in a block
	 * are instantiated via a signature.
	 * 
	 * @param origin
	 *            port
	 * @returns a copy of this port, with possibly its type, tag and visibility
	 *          inherited from port origin.
	 */
	public Port join(Port origin) {
		if (origin == null)
			throw new NullPointerException();
		PortType _type = origin.type == PortType.NONE ? type : origin.type;
		TypeTag _tag = origin.tag == null ? tag : origin.tag;
		boolean _visible = visible && origin.visible;
		return new Port(name, _type, PrioType.NONE, _tag, _visible);
	}

	/**
	 * Gets the name of this port.
	 * 
	 * @return name of this port
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the type of this port.
	 * 
	 * @return port type of this port
	 */
	public PortType getType() {
		return type;
	}

	/**
	 * Gets the priority type of this port.
	 * 
	 * @return priority type of this port
	 */
	public PrioType getPrioType() {
		return prio;
	}

	/**
	 * Gets the type tag of this port.
	 * 
	 * @return type tag of this port
	 */
	public TypeTag getTypeTag() {
		return tag;
	}

	/**
	 * Gets the visibility of this port.
	 * 
	 * @return visibility of this port
	 */
	public boolean isHidden() {
		return !visible;
	}

	/**
	 * Constructs a renamed copy of this port, and preserves all other
	 * information.
	 * 
	 * @param name
	 *            new port name
	 * @return renamed copy of this port
	 */
	public Port rename(String name) {
		return new Port(name, type, prio, tag, visible);
	}

	/**
	 * Constructs a copy of this port with the specified tag.
	 * 
	 * @param tag
	 *            new type tag
	 * @return port with new type tag
	 */
	public Port setTag(TypeTag tag) {
		return new Port(name, type, prio, tag, visible);
	}

	/**
	 * Constructs a copy of this port with visibility set to false.
	 * 
	 * @return invisible copy of this port
	 */
	public Port hide() {
		return new Port(name, type, prio, tag, false);
	}

	/**
	 * Determines whether this port in an input port.
	 * 
	 * @return true if this port is an input, and false otherwise.
	 */
	public boolean isInput() {
		return type == PortType.IN;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return (visible ? "" : "*") + prio + name + type + (tag == null ? "" : tag);
	}

	@Override
	public int compareTo(Port other) {
		return this.name.compareTo(other.name);
	}
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public boolean equals(@Nullable Object other) {
//		if (other == null)
//			return false;
//		if (other == this)
//			return true;
//		if (!(other instanceof Port))
//			return false;
//		Port p = (Port) other;
//		return (Objects.equals(this.name, p.name));
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public int hashCode() {
//		return Objects.hash(this.name);
//	}
}
