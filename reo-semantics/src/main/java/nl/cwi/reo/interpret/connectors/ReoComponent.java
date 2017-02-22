package nl.cwi.reo.interpret.connectors;

import java.util.List;
import java.util.Map;

import nl.cwi.reo.interpret.Expression;
import nl.cwi.reo.interpret.instances.SetExpression;
import nl.cwi.reo.interpret.ports.Port;
import nl.cwi.reo.interpret.terms.Term;
import nl.cwi.reo.interpret.terms.TermList;
import nl.cwi.reo.interpret.variables.VariableListExpression;

/**
 * A SubComponent is a part of a Connector.
 * 
 * A SubComponent is an immutable object.
 * @param <T> type of semantics objects
 * @see CompositeReoComponent
 */
public interface ReoComponent<T extends Semantics<T>> extends Expression<ReoComponent<T>>,Term {
	
	/**
	 * Gets the links from internal ports to external ports.
	 * @return map assigning an internal port to an external port.
	 */
	public Map<Port, Port> getLinks();

	public SetExpression<T> instantiate(TermList values, VariableListExpression ports);

	
	/**
	 * Relabels the set of links of this subcomponent by renaming 
	 * all link targets names according to renaming map, and hiding
	 * all ports that are not renamed.
	 * @param joins		renaming map
	 * @return a copy of this block with reconnected links
	 */
	public ReoComponent<T> reconnect(Map<Port, Port> joins);

	/**
	 * Renames all hidden ports in this subcomponent to an 
	 * integer value, starting from a given integer i. Integer i gets 
	 * incremented to the smallest integer greater or equal to i, that 
	 * not used as a port name.
	 * @param i		start value of hidden ports.
	 * @return Block with renamed hidden ports.
	 */
	public ReoComponent<T> renameHidden(Integer i);
	
	/**
	 * Flattens the nested block structure of this subcomponent. This 
	 * operation is particularly useful if this subcomponent uses only a single 
	 * associative product operator.
	 * @return List of all components contained in this subcomponent.
	 */
	public List<AtomicReoComponent<T>> flatten();
	
	/**
	 * Inserts, if necessary, a merger and/or replicator at every node in this instance list. 
	 * This method uses an instance of a semantics object to create nodes of the correct type.
	 * @param mergers			insert mergers
	 * @param replicators		insert replicators
	 * @param nodeFactory		instance of semantics object
	 */
	public ReoComponent<T> insertNodes(boolean mergers, boolean replicators, T nodeFactory);
	
	/**
	 * Integrates the links of this subcomponent by renaming the interfaces
	 * of the semantic objects in this subcomponent. If possible, first flatten 
	 * the block, to avoid repeated renaming operations on semantics objects.
	 * @return the semantics object with renamed ports
	 */
	public List<T> integrate();
}
