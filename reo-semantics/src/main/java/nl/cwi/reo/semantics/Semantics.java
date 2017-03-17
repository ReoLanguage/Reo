package nl.cwi.reo.semantics;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.cwi.reo.interpret.Expression;
import nl.cwi.reo.interpret.connectors.ReoConnector;
import nl.cwi.reo.interpret.ports.Port;

/**
 * Implementation of a semantics for Reo components.
 * 
 * @param <T>
 *            semantics
 */
public interface Semantics<T> extends Expression<T> {

	/**
	 * Gets the set of ports in the interface.
	 * 
	 * @return Set of ports.
	 */
	public Set<Port> getInterface();

	/**
	 * Gets the type of semantics.
	 * 
	 * @return Type of semantics.
	 */
	public SemanticsType getType();

	/**
	 * Constructs a new node from a set with given set of ports
	 * 
	 * @param node
	 *            set of ports
	 * @return Node with interface given by the set of ports.
	 */
	public T getNode(Set<Port> node);

	/**
	 * Renames ports in the interface according to a set of links
	 * 
	 * @param links
	 *            renaming mapping
	 * @return Component with interface renamed according to the set of links.
	 */
	public T rename(Map<Port, Port> links);

	/**
	 * Composes a list of components into a single component.
	 * 
	 * @param components
	 *            list of components
	 * @return Composition of the list of components.
	 */
	public T compose(List<T> components);

	/**
	 * Restricts the interface of this component to a given collection of ports.
	 * 
	 * @param intface
	 *            collection of ports.
	 * @return Restriction of this component to the collection of ports.
	 */
	public T restrict(Collection<? extends Port> intface);

}