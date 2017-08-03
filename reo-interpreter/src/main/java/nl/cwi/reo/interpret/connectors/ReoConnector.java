package nl.cwi.reo.interpret.connectors;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.Nullable;

import nl.cwi.reo.interpret.Atom;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.util.Monitor;

/**
 * A SubComponent is a part of a Connector.
 * 
 * A SubComponent is an immutable object.
 * 
 * @see ReoConnectorComposite
 */
public interface ReoConnector {

	/**
	 * Gets the component.
	 * 
	 * @return component name, or null if this component is nameless.
	 */
	@Nullable
	public String getName();

	/**
	 * Checks if this connector is empty.
	 * 
	 * @return true if this connector is a composite Reo consisting of an empty
	 *         list of subconnectors, and false otherwise.
	 */
	public boolean isEmpty();

	/**
	 * Gets the links from internal ports to external ports.
	 * 
	 * @return map assigning an internal port to an external port.
	 */
	public Map<Port, Port> getLinks();

	/**
	 * Relabels the set of links of this connector by renaming all link targets
	 * names according to renaming map, and hiding all ports that are not
	 * renamed.
	 * 
	 * @param r
	 *            renaming map
	 * @return a copy of this block with reconnected links
	 */
	public ReoConnector rename(Map<Port, Port> r);

	/**
	 * Inserts, if necessary, a merger and/or replicator at every node in this
	 * instance list. This method uses an instance of a semantics object to
	 * create nodes of the correct type.
	 * 
	 * @param mergers
	 *            insert mergers
	 * @param replicators
	 *            insert replicators
	 * @param nodeFactory
	 *            instance of semantics object
	 * 
	 * @return connector with nodes inserted.
	 */
	public ReoConnector insertNodes(boolean mergers, boolean replicators, Atom nodeFactory);

	/**
	 * Flattens the nested block structure of this connector, and erases any
	 * used-defined composition operator. This operation is particularly useful
	 * if this connector uses only a single associative product operator.
	 * 
	 * @return connector wherein every sub-connector is atomic.
	 */
	public ReoConnector flatten();

	/**
	 * Integrates the links of this connector by renaming the interfaces of the
	 * semantic objects in this connector.
	 * 
	 * @return connector wherein every atomic component has links that map port
	 *         x to port x.
	 */
	public ReoConnector integrate();

	/**
	 * Propagates type tags of ports that must have the same type.
	 * 
	 * @param m
	 *            message container
	 * 
	 * @return connector wherein every port has its inferred type, or null if
	 *         there is a type conflict.
	 */
	@Nullable
	public ReoConnector propagate(Monitor m);

	/**
	 * Gets a partitioning of all ports of this connector, where each part
	 * consists of ports that must have the same type tag.
	 * 
	 * @return partitioning of ports with respect to port type equivalence.
	 */
	public Set<Set<Port>> getTypePartition();

	/**
	 * Gets the interface of this connector.
	 * 
	 * @return set of ports in the interface of this connector
	 */
	public Set<Port> getInterface();

	/**
	 * Gets all atomic sub-connectors in this connector.
	 * 
	 * @return all atomic sub-connectors in this connector
	 */
	public List<ReoConnectorAtom> getAtoms();

}
