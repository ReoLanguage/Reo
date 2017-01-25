package nl.cwi.reo.semantics.api;

import java.util.List;
import java.util.Map;

public interface Block<T extends Semantics<T>> extends Evaluable<Block<T>> {
	
	/**
	 * Gets the links from internal ports to external ports.
	 * @return map assigning an internal port to an external port.
	 */
	public Map<Port, Port> getLinks();

	/**
	 * Connects a link to port p to a port q.
	 * @param joins		a set of joins from p to q
	 * @return a copy of this block with reconnected links
	 */
	public Block<T> connect(Map<Port, Port> joins);

	/**
	 * Renames all hidden ports in this list of instances to an 
	 * integer value, starting from a given integer i. Integer i
	 * increments to the smallest integer greater or equal to i, that 
	 * not used as a port name.
	 * @param i		start value of hidden ports.
	 * @return Block with renamed hidden ports.
	 */
	public Block<T> renameHidden(Integer i);
	
	/**
	 * Flattens the nested block structure of this block. This operation
	 * is particularly useful if the connector uses only a single 
	 * associative product operator.
	 * @return List of all blocks contained in this block.
	 */
	public List<Block<T>> flatten();
	
	/**
	 * Inserts, if necessary, a merger and/or replicator at every node in this instance list. 
	 * This method uses an instance of a semantics object to create nodes of the correct type.
	 * @param mergers			insert mergers
	 * @param replicators		insert replicators
	 * @param nodeFactory		instance of semantics object
	 */
	public Block<T> insertNodes(boolean mergers, boolean replicators, T nodeFactory);
	
	/**
	 * Integrates the links of this block by renaming the interfaces
	 * of the semantic objects in this block. If possible, first flatten 
	 * the block, to avoid repeated renaming operations on semantics objects.
	 * @return the semantics object with renamed ports
	 */
	public List<T> integrate();
}
