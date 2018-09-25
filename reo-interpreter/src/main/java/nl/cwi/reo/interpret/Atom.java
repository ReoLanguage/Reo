package nl.cwi.reo.interpret;

import java.util.Map;
import java.util.Set;

import nl.cwi.reo.interpret.ports.Port;

/**
 * An object that can be used by the Reo interpreter as the semantics of Reo
 * components.
 */
public interface Atom extends Expression<Atom> {
	
	/**
	 * Gets the type of semantics.
	 * 
	 * @return Type of semantics.
	 */
	public SemanticsType getType();

	/**
	 * Gets the set of ports in the interface.
	 * 
	 * @return Set of ports.
	 */
	public Set<Port> getInterface();

	/**
	 * Renames ports in the interface according to a set of links.
	 *
	 * @param links
	 *            renaming mapping
	 * @return Component with interface renamed according to the set of links.
	 */
	public Atom rename(Map<Port, Port> links);

	/**
	 * Constructs a new node from a set with given set of ports. A node
	 * synchronizes a single output port with all input ports.
	 *
	 * @param node
	 *            set of ports
	 * @return Node with interface given by the set of ports.
	 */
	public Atom getNode(Set<Port> node);
	
}
